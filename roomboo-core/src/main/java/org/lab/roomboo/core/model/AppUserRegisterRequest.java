package org.lab.roomboo.core.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppUserRegisterRequest {

	@Email
	@NotNull
	private String email;

	@NotBlank
	private String displayName;

}
