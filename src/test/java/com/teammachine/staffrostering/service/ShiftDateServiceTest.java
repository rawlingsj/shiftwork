package com.teammachine.staffrostering.service;

import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;
import com.teammachine.staffrostering.service.impl.ShiftDateServiceImpl;
import com.teammachine.staffrostering.web.rest.dto.ShiftDateDTO;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Created by asad on 12/22/16.
 */
public class ShiftDateServiceTest {

    ShiftDateDTO shiftDateDTO = new ShiftDateDTO();
    ShiftDate shiftDate = new ShiftDate();
    ShiftDateService service = new ShiftDateServiceImpl();

    /**
     * Populating Input ShiftDateDTO and ShiftDateEntity for test purposes.
     */
    @Before
    public void setShiftDateDTO() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.FRIDAY);
        daysOfWeek.add(DayOfWeek.MONDAY);
        shiftDateDTO.setDaysOfWeek(daysOfWeek);
        shiftDateDTO.setRepeatFor(4);
        shiftDateDTO.setDate(LocalDate.of(2017, 1, 4));
        shiftDate = service.mapDTOToEntity(shiftDateDTO);
    }

    /**
     * Testing the service for number of records/entity-objects generated based on input parameters.
     */
    @Test
    public void testShiftDateServiceForNumberOfRecordsGenerated() {
        List<ShiftDate> shiftDates = service.generateEntitiesUsingRepeatForInShiftDateDTO(shiftDateDTO);
        int actual = 12;
        int expected = shiftDates.size();
        assertEquals(actual, expected);
    }

    /**
     * Testing whether service is extracting the correct DayOfWeek from the input date.
     */
    @Test
    public void testShiftDateService_CheckDayExtractedFromDate() {
        List<ShiftDate> shiftDates = service.generateEntitiesUsingRepeatForInShiftDateDTO(shiftDateDTO);
        Optional<DayOfWeek> expected = Optional.of(DayOfWeek.WEDNESDAY);
        Optional<DayOfWeek> actual = shiftDates.stream().filter(i -> i.getDate() == shiftDate.getDate()).map(i -> i.getDayOfWeek()).findFirst();
        assertEquals(expected, actual);
    }


}
