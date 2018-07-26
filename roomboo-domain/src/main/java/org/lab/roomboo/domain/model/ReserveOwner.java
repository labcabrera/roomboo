package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "reserveOwners")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReserveOwner {

	private String id;

	private String email;

	private String displayName;

	private LocalDateTime activation;

	private LocalDateTime expiration;

	private LocalDateTime locked;

}
