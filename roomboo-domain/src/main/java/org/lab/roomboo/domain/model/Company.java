package org.lab.roomboo.domain.model;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

	public enum SignUpActivationMode {
		AUTO, EMAIL;
	};

	public enum RegisterConfirmationMode {
		AUTO, EMAIL, CODE;
	}

	private String id;

	@NotBlank(message = "validation.Company.name.required")
	@Max(value = 64)
	private String name;

	private String description;

	@NotNull
	private SignUpActivationMode signUpActivationMode;

	@NotNull
	private RegisterConfirmationMode registerConfirmationMode;

}
