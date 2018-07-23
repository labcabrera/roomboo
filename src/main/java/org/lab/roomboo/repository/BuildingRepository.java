package org.lab.roomboo.repository;

import org.lab.roomboo.model.Building;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildingRepository extends MongoRepository<Building, String> {

}
