package org.lab.roomboo.domain.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Document(collection = "tokenReserveConfirmation")
@Getter
@Setter
@SuperBuilder
public class ReserveConfirmationToken extends ConfirmationToken {

	@DBRef
	protected Reserve reserve;
}
