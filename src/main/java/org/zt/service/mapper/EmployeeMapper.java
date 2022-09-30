package org.zt.service.mapper;

import org.mapstruct.*;
import org.zt.domain.Employee;
import org.zt.service.dto.EmployeeDTO;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {}
