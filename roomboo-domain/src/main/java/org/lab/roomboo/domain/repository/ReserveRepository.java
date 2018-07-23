package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.Reserve;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReserveRepository extends MongoRepository<Reserve, String> {

}
