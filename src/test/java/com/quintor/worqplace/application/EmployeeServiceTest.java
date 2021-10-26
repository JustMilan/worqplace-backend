package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.EmployeeNotFoundException;
import com.quintor.worqplace.data.EmployeeRepository;
import com.quintor.worqplace.domain.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmployeeServiceTest {
	private EmployeeService employeeService;
	private EmployeeRepository employeeRepository;
	private Employee employee;

	@BeforeEach
	void initialize() {
		this.employeeRepository = mock(EmployeeRepository.class);
		this.employeeService = new EmployeeService(employeeRepository);
		this.employee = new Employee(1L, "FirstTestname", "LastTestname");

		when(this.employeeRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(employee));
		when(this.employeeRepository.findById(2L)).thenReturn(java.util.Optional.empty());
	}

	@Test
	void getEmployeeByIdShouldReturnEmployeeIfExists() {
		assertEquals(employee, employeeService.getEmployeeById(employee.getId()));
	}

	@Test
	void getEmployeeByIdShouldThrowIfNotExists() {
		assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(2L));
	}
}
