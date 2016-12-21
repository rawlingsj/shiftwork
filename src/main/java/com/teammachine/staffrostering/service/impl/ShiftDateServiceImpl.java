package com.teammachine.staffrostering.service.impl;

import com.teammachine.staffrostering.domain.ShiftDate;
import com.teammachine.staffrostering.repository.ShiftDateRepository;
import com.teammachine.staffrostering.service.ShiftDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
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
}
