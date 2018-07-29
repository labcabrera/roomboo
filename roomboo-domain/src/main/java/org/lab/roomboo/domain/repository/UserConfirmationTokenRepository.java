package org.lab.roomboo.domain.repository;

import java.util.Optional;

import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.lab.roomboo.domain.model.UserConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserConfirmationTokenRepository extends MongoRepository<UserConfirmationToken, String> {

	Optional<ReserveConfirmationToken> findByToken(String token);

}
