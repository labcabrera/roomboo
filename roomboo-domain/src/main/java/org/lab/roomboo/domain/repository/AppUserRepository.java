package org.lab.roomboo.domain.repository;

import java.util.Optional;

import org.lab.roomboo.domain.model.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface AppUserRepository extends MongoRepository<AppUser, String> {

	@Query("{ 'email' : ?0, 'company.id' : ?1 }")
	Optional<AppUser> findByEmail(String email, String companyId);

}
