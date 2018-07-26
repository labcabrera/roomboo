package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.RoomGroup;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BuildingRepository extends MongoRepository<RoomGroup, String> {

}
