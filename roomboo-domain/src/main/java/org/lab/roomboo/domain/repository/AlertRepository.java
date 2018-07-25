package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AlertRepository extends MongoRepository<Alert, String> {

}
