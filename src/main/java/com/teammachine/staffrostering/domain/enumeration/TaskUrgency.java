package com.teammachine.staffrostering.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The TaskUrgency enumeration.
 */
public enum TaskUrgency {
    URGENT("Urgent", 10), NOT_URGENT("Not_Urgent", 2);

    private String code;
    private Integer weight;

    private TaskUrgency(String code, Integer weight) {
        this.code = code;
        this.weight = weight;
    }

    public static TaskUrgency valueOfCode(String code) {
        for (TaskUrgency taskImportance : values()) {
            if (code.equalsIgnoreCase(taskImportance.getCode())) {
                return taskImportance;
            }
        }
        return null;
    }
    @JsonValue
    public String getCode() {
        return code;
    }
    public Integer getWeight() {
        return weight;
    }
}
