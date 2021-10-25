package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.EmployeeNotFoundException;
import com.quintor.worqplace.data.EmployeeRepository;
import com.quintor.worqplace.domain.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class EmployeeService {
	private final EmployeeRepository employeeRepository;

	public Employee getEmployeeById(Long id) {
		return employeeRepository.findById(id).orElseThrow(
				() -> new EmployeeNotFoundException(id));
	}
}
