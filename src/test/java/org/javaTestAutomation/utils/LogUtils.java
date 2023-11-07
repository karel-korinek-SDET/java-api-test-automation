package org.javaTestAutomation.utils;

import org.javaTestAutomation.enums.Severity;
import org.javaTestAutomation.tests.BaseTest;

import static org.apache.commons.lang3.StringUtils.repeat;

public class LogUtils {

    private static final int LOG_MAX_CHARS = 84;

    public static void printTestSuiteName(String testSuiteName) {
        printInfo(DateUtils.getCurrentDateAndTime("yyyy-MM-dd"));
        printUnifiedLogMessage("=".charAt(0), "TEST SUITE: " + testSuiteName, 1);
    }

    public static void printTestNameString(String testName) {
        printUnifiedLogMessage("~".charAt(0), "TEST: " + testName);
    }

    public static void printTestResultString(String testResult) {
        printUnifiedLogMessage("-".charAt(0), testResult);
    }

    public static void printUnifiedLogMessage(Character borders, String message) {
        printUnifiedLogMessage(borders, message, 0);
    }

    public static void printUnifiedLogMessage(Character borders, String message, int borderRows) {
        printBorders(borders, borderRows, LOG_MAX_CHARS);

        String prefix = "";
        String suffix = "";
        if (message.length() < LOG_MAX_CHARS) {
            boolean isEven = message.length() % 2 == 0;
            int borderLength = (LOG_MAX_CHARS - message.length()) / 2;
            prefix += repeat(borders, borderLength);
            suffix += isEven ? "" : " ";
            suffix += repeat(borders, borderLength - 1);
        }
        System.out.println(prefix + " " + message + " " + suffix);
        printBorders(borders, borderRows, LOG_MAX_CHARS);
        if (borderRows > 0) LogUtils.printEmptyLine();
    }

    /**
     * println a character number of times specified by maxChars.
     * Do it as many times as specified by borderRows
     */
    private static void printBorders(char character, int borderRows, int maxChars) {
        if (borderRows > 0) {
            for (int i = 0; i < borderRows; i++) {
                System.out.println(repeat(character, maxChars + 1));
            }
        }
    }

    /**
     * This is used for gitLab pipeline because System.out.println() does not work there.
     */
    public static void printEmptyLine() {
        System.out.println(".");
    }

    public static void printError(String text) {
        if (BaseTest.LOG_LEVEL.getValue() >= Severity.ERROR.getValue())
            System.err.println(DateUtils.getCurrentDateAndTime("HH:mm:ss") + " - " + text);
    }

    public static void printWarning(String text) {
        if (BaseTest.LOG_LEVEL.getValue() >= Severity.WARNING.getValue())
            System.out.println(DateUtils.getCurrentDateAndTime("HH:mm:ss") + " - " + text);
    }

    public static void printInfo(String text) {
        if (BaseTest.LOG_LEVEL.getValue() >= Severity.INFO.getValue())
            System.out.println(DateUtils.getCurrentDateAndTime("HH:mm:ss") + " - " + text);
    }

    public static void printDetails(String text) {
        if (BaseTest.LOG_LEVEL.getValue() >= Severity.DETAILS.getValue())
            System.out.println(DateUtils.getCurrentDateAndTime("HH:mm:ss") + " - " + text);
    }

    public static void printDebug(String text) {
        if (BaseTest.LOG_LEVEL.getValue() >= Severity.DEBUG.getValue())
            System.out.println(DateUtils.getCurrentDateAndTime("HH:mm:ss") + " - " + text);
    }
}
