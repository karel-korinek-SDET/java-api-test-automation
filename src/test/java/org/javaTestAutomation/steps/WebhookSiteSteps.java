package org.javaTestAutomation.steps;

import org.javaTestAutomation.api.WebhookSiteApi;
import org.javaTestAutomation.dto.CreateTokenResponseBodyDto;
import org.javaTestAutomation.dto.DeleteWebhooksResponseBodyDto;
import org.javaTestAutomation.dto.WebhooksDto;
import org.javaTestAutomation.utils.LogUtils;
import org.javaTestAutomation.utils.RetrofitUtils;
import org.javaTestAutomation.utils.SleepUtils;
import org.junit.Assert;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class WebhookSiteSteps {

    private static final String BASE_URL = "https://webhook.site/";
    private static final int WEBHOOK_API_RETRIES = Integer.parseInt(System.getProperty("WEBHOOK_API_RETRIES"));
    private static final int WEBHOOK_CYCLE_SLEEP_LENGTH = Integer.parseInt(System.getProperty("WEBHOOK_CYCLES_SLEEP_LENGTH"));

    private static final WebhookSiteApi webhookSiteApi = RetrofitUtils.getRetrofit(BASE_URL).create(WebhookSiteApi.class);
    private final String token;


    /**
     * Default constructor, creates a new webhook site token
     */
    public WebhookSiteSteps() {
        token = createWebhookSiteWebhook();
    }

    /**
     * Use this  constructor to use an existing token
     */
    public WebhookSiteSteps(String customToken) {
        token = customToken;
    }

    /**
     * GET all webhook site webhooks
     */
    public WebhooksDto getWebhooks() {
        return getWebhooks(null, null);
    }

    /**
     * GET webhooks specified by sorting and a search query
     *
     * @param sorting newest/oldest
     * @param query search string
     *
     * @example getWebhooks("newest", "searchForThis")
     */
    public WebhooksDto getWebhooks(String sorting, String query) {
        LogUtils.printInfo("Getting webhooks...");
        var response = (Response<WebhooksDto>) webhookSiteCall(
                webhookSiteApi.getRequests(token, sorting, query)
        );

        Assert.assertEquals(200, response.code());
        Assert.assertNotNull(response.body());

        var data = response.body().data;
        LogUtils.printInfo("Found " + data.size() + " webhooks.");
        for (int i = 0; i < data.size(); i++) {
            LogUtils.printDetails("#" + (i+1) + ": " + data.get(i).content);
        }
        LogUtils.printEmptyLine();
        return response.body();
    }

    /**
     * DELETE all webhooks from webhook site
     */
    public void deleteWebhooks() {
        LogUtils.printInfo("Deleting webhooks...");
        var response = (Response<DeleteWebhooksResponseBodyDto>) webhookSiteCall(
                webhookSiteApi.deleteRequests(token)
        );

        Assert.assertEquals(200, response.code());
        Assert.assertNotNull(response.body());
        Assert.assertTrue(response.body().status);

        LogUtils.printInfo("Webhooks deleted.");
        LogUtils.printEmptyLine();
    }

    /**
     * POST a custom request to the webhook site
     */
    public void postRequest(Object body) {
        LogUtils.printInfo("Posting custom webhook request...");
        var response = (Response<Void>) webhookSiteCall(
                webhookSiteApi.postRequest(token, body)
        );

        Assert.assertEquals(200, response.code());

        LogUtils.printInfo("Request successfully posted.");
        LogUtils.printEmptyLine();
    }

    /**
     * Create a webhook on the webhook site and return its token
     */
    private static String createWebhookSiteWebhook() {
        LogUtils.printInfo("Creating webhook (webhook site)...");
        var createTokenResponse = (Response<CreateTokenResponseBodyDto>) webhookSiteCall(
                webhookSiteApi.createWebhookToken()
        );

        Assert.assertNotNull("Webhook site createTokenResponse is null", createTokenResponse);
        Assert.assertNotNull("Webhook site body is null. Response: " + createTokenResponse, createTokenResponse.body());
        Assert.assertNotNull("Webhook UUID is null", createTokenResponse.body().uuid);

        LogUtils.printInfo("Webhook site UID: " + createTokenResponse.body().uuid);
        LogUtils.printEmptyLine();
        return createTokenResponse.body().uuid;
    }

    /**
     * Use this for webhook site calls. Catches socket timeout exceptions and unsuccessful responses and then retries.
     */
    private static Response<?> webhookSiteCall(Call<?> call) {
        // Try to execute the call.
        Response<?> response = null;
        for (int i = 0; i < WEBHOOK_API_RETRIES; i++) {
            try {
                response = call.clone().execute();
                // Catch an unsuccessful response
                if (!response.isSuccessful()) {
                    // Unless it's the last iteration, retry.
                    if (i < WEBHOOK_API_RETRIES - 1) {
                        LogUtils.printWarning("Webhook site returned " + response.code() + ". Retrying... (#" + (i + 1) + ")");
                        SleepUtils.sleep(WEBHOOK_CYCLE_SLEEP_LENGTH);
                        continue;
                    }
                }
                // If the response is successful, jump out of the loop
                break;
                // Catch a socket timeout exception
            } catch (SocketTimeoutException socketTimeoutException) {
                // Unless it's the last iteration, retry.
                if (i < WEBHOOK_API_RETRIES - 1) {
                    LogUtils.printWarning("Webhook site socket timeout exception. Retrying... (#" + (i + 1) + ")");
                    SleepUtils.sleep(WEBHOOK_CYCLE_SLEEP_LENGTH);
                    continue;
                }
                // An unexpected exception is thrown without any retry.
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // Throw an exception on the last iteration.
            LogUtils.printError("Retry was unsuccessful.");
            throw new RuntimeException("Retry was unsuccessful.");
        }
        Assert.assertNotNull(response);
        return response;
    }

    /**
     * Get webhooks sorted by newest with query "content:" + search string
     */
    public String waitForRequest(String content) {
        Response<WebhooksDto> response = null;
        LogUtils.printDebug("Looking for a webhook with content query: " + content + "...");
        // Should sleep already on the first iteration, because of the webhook site's speed
        SleepUtils.sleep(WEBHOOK_CYCLE_SLEEP_LENGTH);

        for (int i = 0; i < WEBHOOK_API_RETRIES; i++) {

            // Last iteration
            if (i == (WEBHOOK_API_RETRIES - 1)) {
                throw new RuntimeException("No matching webhooks on webhook site by content: " + content);
            }

            // After first iteration
            if (i > 0) {
                LogUtils.printInfo("No matching webhook request found. \nRetrying... #" + (i + 1));
                SleepUtils.sleep(WEBHOOK_CYCLE_SLEEP_LENGTH);
            }

            response = (Response<WebhooksDto>) webhookSiteCall(
                    webhookSiteApi.getRequests(token, "newest", "content:" + content)
            );

            if (response == null) {
                LogUtils.printError("Webhook site get requests response is null.");
                continue;
            }

            if (response.body() == null) {
                LogUtils.printError("Webhook site get requests response body is null.");
                continue;
            }

            if (response.body().data == null) {
                LogUtils.printError("Webhook site get requests response body data id null");
                continue;
            }

            if (response.body().total == 0) {
                LogUtils.printError("Webhook site get requests response body total is 0: " + response.body());
                continue;
            }

            if (response.body().total > 1) {
                LogUtils.printError("Found more than one matching request: " + response.body().data);
                continue;
            }


            if (response.body().data.size() < 1) {
                LogUtils.printError("Webhook site request data size is lower than 1. Data: " + response.body().data);
                continue;
            }

            if (response.body().data.get(0).content == null) {
                LogUtils.printError("Webhook site request data content is null. Data: " + response.body());
                continue;
            }

            // On success
            if (response.body().total == 1) {
                LogUtils.printDebug("Found exactly one webhook.");
                break;
            }
        }

        // NullPointerExceptions should be handled in the previous code
        LogUtils.printDebug("Webhook site request data content: " + response.body().data.get(0).content);
        LogUtils.printEmptyLine();
        return response.body().data.get(0).content;
    }
}
