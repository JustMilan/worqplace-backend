package com.quintor.worqplace.presentation.dto.employee;

import com.quintor.worqplace.domain.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

	EmployeeDTO toEmployeeDTO(Employee employee);
}
