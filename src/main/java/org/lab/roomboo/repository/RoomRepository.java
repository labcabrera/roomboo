package org.lab.roomboo.repository;

import org.lab.roomboo.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface RoomRepository extends MongoRepository<Room, String> {

	@Query("{ 'building.id' : ?0 }")
	Page<Room> search(String buildingId, Pageable pageable);

}
