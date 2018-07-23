package org.lab.roomboo.repository;

import org.lab.roomboo.model.Company;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CompanyRepository extends MongoRepository<Company, String> {

}
