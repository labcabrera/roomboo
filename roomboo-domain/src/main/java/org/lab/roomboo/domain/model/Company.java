package org.lab.roomboo.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "companies")
@Valid
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

	/**
	 * Establishes how to process the activation of new users.
	 * @author lab.cabrera@gmail.com
	 */
	public enum SignUpActivationMode {
	/** Registered users are automatically approved. */
	AUTO,
	/** New user confirmation by mail is required for new users. */
	EMAIL
	}

	private String id;

	@NotBlank(message = "validation.Company.name.required")
	@Max(value = 64)
	private String name;

	private String description;

	private SignUpActivationMode signUpActivationMode;

}
