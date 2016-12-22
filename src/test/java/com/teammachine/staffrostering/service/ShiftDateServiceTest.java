package com.teammachine.staffrostering.service;

import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;
import com.teammachine.staffrostering.service.impl.ShiftDateServiceImpl;
import com.teammachine.staffrostering.web.rest.dto.ShiftDateDTO;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by asad on 12/22/16.
 */
public class ShiftDateServiceTest {

    ShiftDateDTO shiftDateDTO = new ShiftDateDTO();
    ShiftDate shiftDate = new ShiftDate();
    ShiftDateService service = new ShiftDateServiceImpl();

    @Before
    public void setShiftDateDTO() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.FRIDAY);
        daysOfWeek.add(DayOfWeek.MONDAY);
        shiftDateDTO.setDaysOfWeek(daysOfWeek);
        shiftDateDTO.setRepeatFor(4);
        shiftDateDTO.setDate(LocalDate.of(2017,1,4));
        shiftDate = service.mapDTOToEntity(shiftDateDTO);
    }

    @Test
    public void testShiftDateService_NumberOfRecordsGenerated() {
        List<ShiftDate> shiftDates = service.generateEntitiesUsingRepeatForInShiftDateDTO(shiftDateDTO);
        assertEquals(12,shiftDates.size());
    }

    @Test
    public void testShiftDateService_CheckDayExtractedFromDate() {
        List<ShiftDate> shiftDates = service.generateEntitiesUsingRepeatForInShiftDateDTO(shiftDateDTO);
        Optional<DayOfWeek> expected = Optional.of(DayOfWeek.WEDNESDAY);
        Optional<DayOfWeek> actual = shiftDates.stream().filter(i->i.getDate() == shiftDate.getDate()).map(i->i.getDayOfWeek()).findFirst();
        assertEquals(expected, actual);
    }


}
