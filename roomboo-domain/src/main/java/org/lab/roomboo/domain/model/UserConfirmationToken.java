package org.lab.roomboo.domain.model;

import javax.validation.constraints.NotNull;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Document(collection = "tokenUserConfirmation")
@Getter
@Setter
@SuperBuilder
public class UserConfirmationToken extends ConfirmationToken {

	@DBRef
	@NotNull
	private AppUser user;

}
