package org.javaTestAutomation.enums;

public enum Severity {
    ERROR(1),
    WARNING(2),
    INFO(3),
    DETAILS(4),
    DEBUG(5);

    private final int value;

    Severity(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Severity fromValue(int value) {
        switch (value) {
            case 0:
            case 1:
                return ERROR;
            case 2: return WARNING;
            case 3: return INFO;
            case 4: return DETAILS;
            case 5: return DEBUG;
            default: throw new RuntimeException("Severity out of range");
        }
    }
}