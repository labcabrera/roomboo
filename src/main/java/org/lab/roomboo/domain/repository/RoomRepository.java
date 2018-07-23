package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {

}
