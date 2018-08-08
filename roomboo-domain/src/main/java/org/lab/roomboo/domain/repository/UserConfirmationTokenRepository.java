package org.lab.roomboo.domain.repository;

import java.util.Optional;

import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserConfirmationTokenRepository extends MongoRepository<UserConfirmationToken, String> {

	Optional<UserConfirmationToken> findByToken(String token);

	Optional<UserConfirmationToken> findByUserId(String userId);
	
	void deleteByUserId(String userId);

}
