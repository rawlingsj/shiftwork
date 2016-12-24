package com.teammachine.staffrostering.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.service.EmployeeService;
import com.teammachine.staffrostering.web.rest.dto.EmployeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping({"/api/fields"})
public class FieldResource {

    private final Logger log = LoggerFactory.getLogger(FieldResource.class);

    @Inject
    private EmployeeService employeeService;

    @RequestMapping(value = "/employees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<EmployeeDTO> findByCodeOrName(@RequestParam("like") String like) {
        log.debug("Request to find employees by substring in code or name");
        if (like.isEmpty()) {
            return Collections.emptyList();
        }
        return employeeService.findAllByCodeLikeIgnoreCaseOrNameLikeIgnoreCase(like).stream()
            .map(this::asDTO)
            .sorted(Comparator.comparing(e -> e.getName().toLowerCase()))
            .collect(Collectors.toList());
    }

    private EmployeeDTO asDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(employee, employeeDTO);
        return employeeDTO;
    }
}
