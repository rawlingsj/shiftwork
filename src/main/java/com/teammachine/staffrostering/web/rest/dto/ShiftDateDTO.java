package com.teammachine.staffrostering.web.rest.dto;

import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;

import java.time.LocalDate;
import java.util.Set;

/**
 * Created by asad on 12/14/16.
 */
public class ShiftDateDTO {

    private Long id;
    private Integer dayIndex;
    private LocalDate date;
    private Set<DayOfWeek> daysOfWeek;
    private Integer repeatFor;


    public Integer getRepeatFor() {
        return repeatFor;
    }

    public void setRepeatFor(Integer repeatFor) {
        this.repeatFor = repeatFor;
    }

    public Set<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Set<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
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
