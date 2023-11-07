package org.javaTestAutomation.rules;

import org.javaTestAutomation.enums.TestStatus;
import org.javaTestAutomation.tests.BaseTest;
import org.javaTestAutomation.utils.LogUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public abstract class LoggingRule implements TestRule {
    public Statement apply(Statement base, Description description) {
        return statement(base);
    }

    private Statement statement(final Statement base) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                before();
                try {
                    // Execute the test
                    base.evaluate();
                    // If no AssertionError or Exception, mark it as PASSED
                    BaseTest.testStatus = TestStatus.PASSED;
                }
                // This could be used for catching "Assume" exceptions:
                /*catch (org.junit.AssumptionViolatedException ignored) {}*/
                catch (AssertionError | Exception throwable) {
                    LogUtils.printError(throwable.toString());
                    throw throwable;
                } finally {
                    after();
                }
            }
        };
    }

    /**
     * Override to set up your specific external resource.
     */
    protected void before() {
    }

    /**
     * Override to tear down your specific external resource.
     */
    protected void after() {
        LogUtils.printTestResultString("RESULT: " + BaseTest.testStatus.toString());
        LogUtils.printEmptyLine();
    }
}
