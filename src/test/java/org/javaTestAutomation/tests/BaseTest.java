package org.javaTestAutomation.tests;

import org.javaTestAutomation.enums.Severity;
import org.javaTestAutomation.enums.TestStatus;
import org.javaTestAutomation.rules.LoggingRule;
import org.javaTestAutomation.rules.RetryRule;
import org.javaTestAutomation.steps.WebhookSiteSteps;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class BaseTest {

    public static final Severity LOG_LEVEL = Severity.fromValue(Integer.parseInt(System.getProperty("LOG_LEVEL")));
    public static final int RETRY_COUNT = Integer.parseInt(System.getProperty("RETRY_COUNT"));
    protected static final WebhookSiteSteps webhookSiteSteps = new WebhookSiteSteps();
    public static TestStatus testStatus;

    static { System.out.println("Log level set to " + LOG_LEVEL); }

    @Rule
    public LoggingRule loggingRule = new LoggingRule() {
        @Override
        public Statement apply(Statement base, Description description) {
            return super.apply(base, description);
        }
    };

    @Rule
    public RetryRule retryRule = new RetryRule(RETRY_COUNT);

    @Before
    public void before() {
        testStatus = TestStatus.IGNORED;
    }

}
