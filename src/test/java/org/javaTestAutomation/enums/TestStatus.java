package org.javaTestAutomation.enums;

public enum TestStatus {
    PASSED("PASSED"),
    IGNORED("IGNORED"),
    FAILED("FAILED");

    private final String text;

    TestStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
