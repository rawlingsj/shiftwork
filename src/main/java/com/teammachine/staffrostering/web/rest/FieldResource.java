package com.teammachine.staffrostering.web.rest;

import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.dto.EmployeeDTO;
import com.teammachine.staffrostering.service.TypeaheadService;

@RestController
@RequestMapping({ "/api", "/api_basic" })
public class FieldResource {

	private final Logger log = LoggerFactory.getLogger(FieldResource.class);

	@Inject
	private TypeaheadService typeaheadService;

	@RequestMapping(value = "/fields/employees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<EmployeeDTO>> findByCodeOrName(@RequestParam("like") String like) {

		log.debug("Filter employees by name or code entered");
		List<EmployeeDTO> employeeDTOs = typeaheadService.processTypeaheadReqOfEmployee(like);
		if (employeeDTOs.isEmpty()) {
			return new ResponseEntity<List<EmployeeDTO>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(employeeDTOs, HttpStatus.OK);

	}

}
