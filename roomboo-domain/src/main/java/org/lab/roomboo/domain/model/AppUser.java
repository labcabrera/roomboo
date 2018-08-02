package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "appUser")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

	private String id;

	private String email;

	private String displayName;

	private LocalDateTime created;

	private LocalDateTime activation;

	private LocalDateTime expiration;

	private LocalDateTime locked;

	private List<Company> companies;

}
