package org.lab.roomboo.domain.repository;

import org.lab.roomboo.domain.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUser, String> {

}
