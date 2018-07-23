package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.ReserveOwner;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReserveOwnerRepository extends MongoRepository<ReserveOwner, String> {

}
