package org.javaTestAutomation.tests;

import com.google.gson.Gson;
import org.javaTestAutomation.dto.PersonDto;
import org.javaTestAutomation.enums.TestStatus;
import org.javaTestAutomation.utils.LogUtils;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;
import java.util.Objects;

@RunWith(Parameterized.class)
public class ParameterizedTest extends BaseTest{

    static {
        LogUtils.printTestSuiteName("Parameterized test");
    }

    private final PersonDto testPerson;

    public ParameterizedTest(PersonDto person) {
        this.testPerson = person;
        // This can be used to skip tests by parameter
        Assume.assumeFalse(Objects.equals(person.lastName, "Malfoy"));
    }

    @Parameterized.Parameters
    public static List<PersonDto> getTestData() {
        return List.of(
                new PersonDto("Albert", "Einstein", true, 179),
                new PersonDto("Ben", "Cristo", false, 199),
                // This one will be skipped
                new PersonDto("Lucius", "Malfoy", true, 191),
                new PersonDto("Daniel", "Jackson", false, 182)
        );
    }

    @Test
    public void parameterizedTest() {
        BaseTest.testStatus = TestStatus.FAILED;
        LogUtils.printTestNameString("Parameterized test with last name - " + testPerson.lastName);

        webhookSiteSteps.deleteWebhooks();
        webhookSiteSteps.postRequest(testPerson);
        var response = webhookSiteSteps.getWebhooks();

        Assert.assertEquals(1, response.total);
        Assert.assertEquals(1, response.data.size());

        var body = new Gson().fromJson(response.data.get(0).content, PersonDto.class);
        Assert.assertEquals(testPerson.fistName, body.fistName);
        Assert.assertEquals(testPerson.lastName, body.lastName);
        Assert.assertEquals(testPerson.marriageStatus, body.marriageStatus);
        Assert.assertEquals(testPerson.height, body.height);
    }
}
