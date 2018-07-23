package org.lab.roomboo.repository;

import org.lab.roomboo.model.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {

}
