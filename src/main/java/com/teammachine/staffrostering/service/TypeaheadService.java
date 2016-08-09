package com.teammachine.staffrostering.service;

import java.util.List;

import com.teammachine.staffrostering.domain.dto.EmployeeDTO;

public interface TypeaheadService {

	public List<EmployeeDTO> processTypeaheadReqOfEmployee(String like);

}
