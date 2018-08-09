package org.lab.roomboo.core.model;

import org.lab.roomboo.core.validation.ValidSignUpRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ValidSignUpRequest
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserSearchOptions {

	/**
	 * Value used to search (case insensitive) in user name, lastName and email.
	 */
	private String text;

	private String email;

	private String companyId;

}
