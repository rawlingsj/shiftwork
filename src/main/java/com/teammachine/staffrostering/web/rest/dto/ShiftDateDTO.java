package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;

import java.time.LocalDate;

/**
 * Created by asad on 12/14/16.
 */
public class ShiftDateDTO {

    private Long id;
    private Integer dayIndex;
    private LocalDate date;
    private DayOfWeek dayOfWeek;
    private DayOfWeek[] daysOfWeek;
    private Integer repeatFor;


    public Integer getRepeatFor() {
        return repeatFor;
    }

    public void setRepeatFor(Integer repeatFor) {
        this.repeatFor = repeatFor;
    }

    public DayOfWeek[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(DayOfWeek[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getDayIndex() {
        return dayIndex;
    }

    public void setDayIndex(Integer dayIndex) {
        this.dayIndex = dayIndex;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
