package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

}
