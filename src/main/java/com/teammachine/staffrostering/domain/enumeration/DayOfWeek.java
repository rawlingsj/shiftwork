package com.teammachine.staffrostering.domain.enumeration;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * The DayOfWeek enumeration.
 */
public enum DayOfWeek implements Serializable {
    MONDAY("Monday"), TUESDAY("Tuesday"), WEDNESDAY("Wednesday"), THURSDAY("Thursday"), FRIDAY("Friday"), SATURDAY("Saturday"), SUNDAY(
        "Sunday");

    /**
     */
    private static Map<String, DayOfWeek> identifierMap = new HashMap<String, DayOfWeek>();
    static {
        for (DayOfWeek value : DayOfWeek.values()) {
            identifierMap.put(value.getValue(), value);
        }
    }

    private String value;

    /**
     */
    private DayOfWeek(String value) {
        this.value = value;
    }

    public static DayOfWeek fromValue(String value) {
        DayOfWeek result = identifierMap.get(value);
        if (result == null) {
            throw new IllegalArgumentException("No DayOfWeek for value: " + value);
        }
        return result;
    }

    public static DayOfWeek toEnum(Object key) {
        if (!(key instanceof String)) {
            throw new IllegalArgumentException("key is not of type String");
        }
        return fromValue((String) key);
    }

    public Object toData() {
        return getValue();
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name();
    }
}
