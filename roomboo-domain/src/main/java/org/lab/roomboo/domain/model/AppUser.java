package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

	@NotBlank
	@NonNull
	private String email;

	@NotBlank
	@NonNull
	private String name;

	private String lastName;

	@NotNull
	private LocalDateTime created;

	private LocalDateTime activation;

	private LocalDateTime expiration;

	private LocalDateTime locked;

	@NotNull
	@DBRef
	private Company company;
	
	public AppUser(String id) {
		this.id = id;
	}

	public Object getCompleteName() {
		return name + " " + lastName;
	}

}
