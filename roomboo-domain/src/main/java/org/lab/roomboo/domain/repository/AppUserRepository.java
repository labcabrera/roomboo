package org.lab.roomboo.domain.repository;

import java.util.Optional;

import org.lab.roomboo.domain.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AppUserRepository extends MongoRepository<AppUser, String> {

	Optional<AppUser> findByEmail(String email);

}
