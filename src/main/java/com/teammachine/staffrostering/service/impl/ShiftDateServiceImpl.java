package com.teammachine.staffrostering.service.impl;

import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.domain.enumeration.DayOfWeek;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.service.ShiftDateService;
import com.teammachine.staffrostering.web.rest.dto.ShiftDateDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asad on 12/21/16.
 */
@Service
@Transactional
public class ShiftDateServiceImpl implements ShiftDateService {

    private final Logger log = LoggerFactory.getLogger(ShiftDate.class);

    @Inject
    private ShiftDateRepository shiftDateRepository;

    @Override
    public ShiftDate save(ShiftDate shiftDate) {
        return shiftDateRepository.save(shiftDate);
    }

    @Override
    public List<ShiftDate> findAll() {
        return shiftDateRepository.findAll();
    }

    @Override
    public ShiftDate findOne(Long id) {
        return shiftDateRepository.findOne(id);
    }

    @Override
    public void delete(Long id) {
        shiftDateRepository.delete(id);

    }

    /**
     * This method read shiftDateDTO and generate "N" no of records in DB, depending upon shiftDateDTO.getRepeatFor() and shiftDateDTO.getDaysOfWeek()
     *
     * @param shiftDateDTO
     */
    @Override
    public List<ShiftDate> generateRecords(ShiftDateDTO shiftDateDTO) {
        List<ShiftDate> shiftDates = new ArrayList<>();
        LocalDate date = shiftDateDTO.getDate();
        DayOfWeek[] daysOfWeek = shiftDateDTO.getDaysOfWeek();
        for (int rf = 0; rf < shiftDateDTO.getRepeatFor(); rf++) {
            for (int dayIndex = 0; dayIndex < daysOfWeek.length; dayIndex++) {
                int range = rf * 7 + (mapDayToNumber(daysOfWeek[dayIndex]) - mapDayToNumber(getDayOfWeekFromDate(date)));
                shiftDateDTO.setDate(date.plusDays(range));
                shiftDateDTO.setDayOfWeek(getDayOfWeekFromDate(shiftDateDTO.getDate()));
                shiftDates.add(mapDTOToEntity(shiftDateDTO));
            }
        }
//        shiftDates.forEach(shiftDate -> save(shiftDate));
        return shiftDates;

    }

    /**
     * Convert ShiftDateDTO to ShiftDate Entity
     * @param dto
     * @return ShiftDateEntity
     */
    @Override
    public ShiftDate mapDTOToEntity(ShiftDateDTO dto) {
        ShiftDate shiftDate = new ShiftDate();
        shiftDate.setId(dto.getId());
        shiftDate.setDayOfWeek(dto.getDayOfWeek());
        shiftDate.setDate(dto.getDate());
        shiftDate.setDayIndex(dto.getDayIndex());
        return shiftDate;
    }

    /**
     * One-To-One Mapping [MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY] = [1, 2, 3, 4, 5, 6, 7]
     *
     * @param dayOfWeek
     * @return
     */
    private int mapDayToNumber(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return 1;
            case TUESDAY:
                return 2;
            case WEDNESDAY:
                return 3;
            case THURSDAY:
                return 4;
            case FRIDAY:
                return 5;
            case SATURDAY:
                return 6;
            case SUNDAY:
                return 7;
            default:
                return 0;
        }
    }

    private DayOfWeek getDayOfWeekFromDate(LocalDate date) {
        return DayOfWeek.valueOf(String.valueOf(date.getDayOfWeek()));
    }


}
