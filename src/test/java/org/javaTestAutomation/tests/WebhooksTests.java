package org.javaTestAutomation.tests;

import com.google.gson.Gson;
import org.javaTestAutomation.dto.PersonDto;
import org.javaTestAutomation.enums.TestStatus;
import org.javaTestAutomation.utils.LogUtils;
import org.junit.Assert;
import org.junit.Test;

public class WebhooksTests extends BaseTest {

    static {
        LogUtils.printTestSuiteName("Webhooks tests");
    }

    @Test
    public void getWebhooksTest() {
        BaseTest.testStatus = TestStatus.FAILED;
        LogUtils.printTestNameString("GET webhooks");
        webhookSiteSteps.deleteWebhooks();

        var getWebhooksResponseBody1 = webhookSiteSteps.getWebhooks();
        Assert.assertEquals(0, getWebhooksResponseBody1.total);

        var testPerson = new PersonDto("Karel", "Korinek", false, 185);
        webhookSiteSteps.postRequest(testPerson);

        var webhookContentString = webhookSiteSteps.waitForRequest(testPerson.lastName);
        var webhookContent = new Gson().fromJson(webhookContentString, PersonDto.class);
        Assert.assertEquals(testPerson.fistName, webhookContent.fistName);
        Assert.assertEquals(testPerson.lastName, webhookContent.lastName);
        Assert.assertEquals(testPerson.marriageStatus, webhookContent.marriageStatus);
        Assert.assertEquals(testPerson.height, webhookContent.height);
    }

    @Test
    public void failingTest() {
        BaseTest.testStatus = TestStatus.FAILED;
        LogUtils.printTestNameString("Failing test");

        var response = webhookSiteSteps.getWebhooks();
        Assert.assertEquals(10, response.total);
    }
}
