package org.lab.roomboo.domain.repository;

import java.util.Optional;

import org.lab.roomboo.domain.model.ApiUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ApiUserRepository extends MongoRepository<ApiUser, String> {

	Optional<ApiUser> findByUsername(String username);

}
