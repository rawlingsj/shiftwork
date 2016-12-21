package com.teammachine.staffrostering.service;


import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.web.rest.dto.ShiftDateDTO;

import java.util.List;

/**
 * Created by asad on 12/21/16.
 */
public interface ShiftDateService {
    ShiftDate save(ShiftDate shiftDate);
    List<ShiftDate> findAll();
    ShiftDate findOne(Long id);
    void delete(Long id);
    void MapDtoToEntity(ShiftDate entity, ShiftDateDTO dto);
    void generateRecords(ShiftDateDTO shiftDateDTO, ShiftDate shiftDateEntity);
}
