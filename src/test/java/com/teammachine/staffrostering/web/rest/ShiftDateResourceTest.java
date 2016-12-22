package com.teammachine.staffrostering.web.rest;

import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;
import com.teammachine.staffrostering.service.ShiftDateService;
import com.teammachine.staffrostering.web.rest.dto.ShiftDateDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by asad on 12/22/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ShiftDateResourceTest {

    ShiftDateDTO shiftDateDTO = new ShiftDateDTO();
    ShiftDate shiftDate = new ShiftDate();

    @Mock
    ShiftDateService service;

    @InjectMocks
    ShiftDateResource resource = new ShiftDateResource();

    @Before
    public void setShiftDateDTO() {
        Set<DayOfWeek> daysOfWeek = new HashSet<>();
        daysOfWeek.add(DayOfWeek.FRIDAY);
        daysOfWeek.add(DayOfWeek.MONDAY);
        shiftDateDTO.setDaysOfWeek(daysOfWeek);
        shiftDateDTO.setRepeatFor(4);
        shiftDateDTO.setDate(LocalDate.of(2017, 1, 4));
        shiftDate.setId(1L);
        shiftDate.setDayIndex(2);
        shiftDate.setDate(shiftDateDTO.getDate());
        shiftDate.setDayOfWeek(DayOfWeek.WEDNESDAY);

    }

    @Test
    public void testPost() throws URISyntaxException {
        List<ShiftDate> shiftDates = Arrays.asList(shiftDate);
        when(service.generateEntitiesUsingRepeatForInShiftDateDTO(shiftDateDTO)).thenReturn(shiftDates);
        ResponseEntity<List<ShiftDate>> responseEntity = resource.createShiftDate(shiftDateDTO);
        HttpStatus expected = HttpStatus.valueOf(200);
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);

    }

    @Test
    public void testPostWithRepeatForEqualsZero() throws URISyntaxException {
        List<ShiftDate> shiftDates = Arrays.asList(shiftDate);
        when(service.generateEntitiesUsingRepeatForInShiftDateDTO(shiftDateDTO)).thenReturn(shiftDates);
        shiftDateDTO.setRepeatFor(0);
        ResponseEntity<List<ShiftDate>> responseEntity = resource.createShiftDate(shiftDateDTO);
        HttpStatus expected = HttpStatus.valueOf(406);
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }
}
