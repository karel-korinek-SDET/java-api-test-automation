package org.javaTestAutomation.rules;

import org.javaTestAutomation.utils.LogUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.Objects;

public class RetryRule implements TestRule {
    private final int retryCount;

    public RetryRule(int retryCount) {
        this.retryCount = retryCount;
    }

    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }

    private Statement statement(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;
                for (int i = 0; i <= retryCount; i++) {
                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        caughtThrowable = t;
                        LogUtils.printWarning(t.toString());
                        LogUtils.printError(description.getDisplayName() + ": run " + (i + 1) + " failed.");
                    }
                }
                LogUtils.printError(description.getDisplayName() + ": Giving up after " + (retryCount + 1) + " failures.");
                throw Objects.requireNonNull(caughtThrowable);
            }
        };
    }
}
