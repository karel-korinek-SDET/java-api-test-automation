package org.javaTestAutomation.api;

import org.javaTestAutomation.dto.CreateTokenResponseBodyDto;
import org.javaTestAutomation.dto.DeleteWebhooksResponseBodyDto;
import org.javaTestAutomation.dto.WebhooksDto;
import retrofit2.Call;
import retrofit2.http.*;

public interface WebhookSiteApi {
    @POST("token")
    Call<CreateTokenResponseBodyDto> createWebhookToken();

    @GET("token/{token}/requests")
    Call<WebhooksDto> getRequests(
            @Path("token") String token,
            @Query("sorting") String sorting,
            @Query("query") String query
    );

    @DELETE("token/{token}/request")
    Call<DeleteWebhooksResponseBodyDto> deleteRequests(
            @Path("token") String token
    );

    @POST("{token}")
    Call<Void> postRequest(
            @Path("token") String token,
            @Body Object object
    );
}

