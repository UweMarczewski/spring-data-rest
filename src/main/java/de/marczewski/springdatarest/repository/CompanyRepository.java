package de.marczewski.springdatarest.repository;

import de.marczewski.springdatarest.domain.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "companies", path = "companies")
public interface CompanyRepository extends CrudRepository<Company, Long> {
}
