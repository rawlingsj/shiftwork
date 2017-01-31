package com.teammachine.staffrostering.domain.util;

import com.teammachine.staffrostering.domain.util.JSR310DateConverters.DateToLocalDateTimeConverter;
import com.teammachine.staffrostering.domain.util.JSR310DateConverters.DateToZonedDateTimeConverter;
import com.teammachine.staffrostering.domain.util.JSR310DateConverters.LocalDateTimeToDateConverter;
import com.teammachine.staffrostering.domain.util.JSR310DateConverters.ZonedDateTimeToDateConverter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class JSR310PersistenceConverters {

    private JSR310PersistenceConverters() {}

    @Converter(autoApply = true)
    public static class LocalDateConverter implements AttributeConverter<LocalDate, java.sql.Date> {

        @Override
        public java.sql.Date convertToDatabaseColumn(LocalDate date) {
            return date == null ? null : java.sql.Date.valueOf(date);
        }

        @Override
        public LocalDate convertToEntityAttribute(java.sql.Date date) {
            return date == null ? null : date.toLocalDate();
        }
    }

    @Converter(autoApply = true)
    public static class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, Date> {

        @Override
        public Date convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
            return ZonedDateTimeToDateConverter.INSTANCE.convert(zonedDateTime);
        }

        @Override
        public ZonedDateTime convertToEntityAttribute(Date date) {
            return DateToZonedDateTimeConverter.INSTANCE.convert(date);
        }
    }

    @Converter(autoApply = true)
    public static class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

        @Override
        public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
            return LocalDateTimeToDateConverter.INSTANCE.convert(localDateTime);
        }

        @Override
        public LocalDateTime convertToEntityAttribute(Date date) {
            return DateToLocalDateTimeConverter.INSTANCE.convert(date);
        }
    }

    @Converter(autoApply = true)
    public static class LocalTimeConverter implements AttributeConverter<LocalTime, String> {

        @Override
        public String convertToDatabaseColumn(LocalTime localTime) {
            if (localTime == null) {
                return null;
            }
            String stringTime = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
            if (stringTime.length() > 8) { // 00:00:00.0000
                stringTime = stringTime.substring(0, 7);
            }
            return stringTime;
        }

        @Override
        public LocalTime convertToEntityAttribute(String s) {
            return s != null ? LocalTime.parse(s, DateTimeFormatter.ISO_LOCAL_TIME) : null;
        }
    }
}
