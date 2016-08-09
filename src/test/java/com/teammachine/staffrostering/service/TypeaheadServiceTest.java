package com.teammachine.staffrostering.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.teammachine.staffrostering.ShiftworkApp;
import com.teammachine.staffrostering.domain.Employee;
import com.teammachine.staffrostering.repository.EmployeeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ShiftworkApp.class)
@WebAppConfiguration
@IntegrationTest
@Transactional
public class TypeaheadServiceTest {

	private static final String DEFAULT_CODE = "AAAAA";
	private static final Boolean DEFAULT_IS_SICK = false;
	private static final String DEFAULT_NAME = "ShiftWorkers";

	@Inject
	private EmployeeRepository employeeRepository;

	@Inject
	private TypeaheadService typeaheadService;

	private Employee employee;

	@Before
	public void initTest() {
		employee = new Employee();
		employee.setCode(DEFAULT_CODE);
		employee.setIsSick(DEFAULT_IS_SICK);
		employee.setName(DEFAULT_NAME);
	}

	@Test
	public void processEmployeeRequestWithLike() {
		employeeRepository.save(employee);
		assertThat(typeaheadService.processTypeaheadReqOfEmployee("shift").size() > 0);
	}

	@Test
	public void processEmployeeRequestWithNoContent() {
		employeeRepository.save(employee);
		assertThat(typeaheadService.processTypeaheadReqOfEmployee("nofield").isEmpty());
	}

	@Test
	public void processEmployeeRequestWithAllContent() {
		employeeRepository.save(employee);
		assertThat(typeaheadService.processTypeaheadReqOfEmployee(null).size() > 0);
	}
}
