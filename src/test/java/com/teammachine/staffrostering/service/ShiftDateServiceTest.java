package com.teammachine.staffrostering.service;

import com.redis.S;
import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.service.impl.ShiftDateServiceImpl;
import com.teammachine.staffrostering.web.rest.dto.ShiftDateDTO;
import net.sf.saxon.type.AnyItemType;
import org.apache.xmlbeans.impl.xb.xsdschema.impl.AnyDocumentImpl;
import org.assertj.core.condition.AnyOf;
import org.boon.core.Sys;
import org.junit.Before;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        shiftDateDTO.setId(1L);
        shiftDateDTO.setDaysOfWeek(daysOfWeek);
        shiftDateDTO.setRepeatFor(4);
        shiftDateDTO.setDate(LocalDate.of(2017,1,4));
        shiftDateDTO.setDayIndex(0);
        shiftDate = service.mapDTOToEntity(shiftDateDTO);
    }

    @Test
    public void testShiftDateService_NumberOfRecordsGenerated() {
        List<ShiftDate> shiftDates = service.generateRecords(shiftDateDTO);
        assertEquals(12,shiftDates.size());
    }

    @Test
    public void testShiftDateService_CheckDayExtractedFromDate() {
        List<ShiftDate> shiftDates = service.generateRecords(shiftDateDTO);
        Optional<DayOfWeek> expected = Optional.of(DayOfWeek.WEDNESDAY);
        Optional<DayOfWeek> actual = shiftDates.stream().filter(i->i.getDate() == shiftDate.getDate()).map(i->i.getDayOfWeek()).findFirst();
        assertEquals(expected, actual);
    }


}
