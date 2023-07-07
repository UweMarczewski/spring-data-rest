package de.marczewski.springdatarest.repository;

import de.marczewski.springdatarest.domain.Employee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "employee", path = "employee")
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
