package com.teammachine.staffrostering.domain.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The TaskImportance enumeration.
 */
public enum TaskImportance {
    IMPORTANT("Important", 10), NOT_IMPORTANT("Not_Important", 1);

    private String code;
    private Integer weight;

    private TaskImportance(String code, Integer weight) {
        this.code = code;
        this.weight = weight;
    }

    @JsonCreator
    public static TaskImportance valueOfCode(String code) {
        for (TaskImportance taskImportance : values()) {
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
