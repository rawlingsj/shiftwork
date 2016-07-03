package com.teammachine.staffrostering.domain.enumeration;

/**
 * The DayOfWeek enumeration.
 */
public enum DayOfWeek {
    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private final String code;

    DayOfWeek(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
