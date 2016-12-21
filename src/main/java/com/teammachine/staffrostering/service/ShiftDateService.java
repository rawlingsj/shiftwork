package com.teammachine.staffrostering.service;


import com.teammachine.staffrostering.domain.ShiftDate;

import java.util.List;

/**
 * Created by asad on 12/21/16.
 */
public interface ShiftDateService {
    ShiftDate save(ShiftDate shiftDate);
    List<ShiftDate> findAll();
    ShiftDate findOne(Long id);
    void delete(Long id);

}
