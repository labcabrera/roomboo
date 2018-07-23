package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.RoomReserve;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomReserveRepository extends MongoRepository<RoomReserve, String> {

}
