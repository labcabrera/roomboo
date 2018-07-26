package org.lab.roomboo.domain.model;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Document(collection = "tokenReserveConfirmation")
@Getter
@Setter
public class ReserveConfirmationToken extends ConfirmationToken {

	@DBRef
	protected Reserve reserve;
}
