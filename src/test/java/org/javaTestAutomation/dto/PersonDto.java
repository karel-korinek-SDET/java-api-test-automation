package org.javaTestAutomation.dto;

public class PersonDto {
    public final String fistName;
    public final String lastName;
    public final boolean marriageStatus;
    public final int height;

    public PersonDto(String fistName, String lastName, boolean marriageStatus, int height) {
        this.fistName = fistName;
        this.lastName = lastName;
        this.marriageStatus = marriageStatus;
        this.height = height;
    }
}
