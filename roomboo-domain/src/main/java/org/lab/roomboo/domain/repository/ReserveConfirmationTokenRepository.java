package org.lab.roomboo.domain.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.lab.roomboo.domain.model.ReserveConfirmationToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ReserveConfirmationTokenRepository extends MongoRepository<ReserveConfirmationToken, String> {

	@Query("{}")
	Optional<ReserveConfirmationToken> findValidToken(String reserveId, LocalDateTime date);

}
