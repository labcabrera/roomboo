package org.lab.roomboo.domain.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ConfirmationToken {

	protected String id;

	protected String token;

	protected LocalDateTime created;

	protected LocalDateTime expiration;

	protected String confirmationUri;
	
	protected String cancelationUri;

}
