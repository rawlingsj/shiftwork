package com.teammachine.staffrostering.domain.enumeration;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.NoSuchElementException;

/**
 * The DurationUnit enumeration.
 */
public enum DurationUnit {
    HOURS(ChronoUnit.HOURS),
    DAYS(ChronoUnit.DAYS),
    WEEKS(ChronoUnit.WEEKS),
    MONTHS(ChronoUnit.MONTHS),
    YEARS(ChronoUnit.YEARS);

    private TemporalUnit temporalUnit;

    DurationUnit(TemporalUnit temporalUnit) {
        this.temporalUnit = temporalUnit;
    }

    public TemporalUnit getTemporalUnit() {
        return temporalUnit;
    }

    public static DurationUnit valueOf(TemporalUnit temporalUnit) {
        for (DurationUnit value : values()) {
            if (value.temporalUnit == temporalUnit) {
                return value;
            }
        }
        throw new NoSuchElementException(temporalUnit.toString());
    }
}
