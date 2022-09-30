package org.zt.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.zt.domain.Employee;

/**
 * Spring Data JPA repository for the Employee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {}
