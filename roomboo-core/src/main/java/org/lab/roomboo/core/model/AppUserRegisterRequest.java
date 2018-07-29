package org.lab.roomboo.core.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.lab.roomboo.core.validation.ValidAppUserRegisterRequest;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ValidAppUserRegisterRequest
@Getter
@Setter
@ToString
public class AppUserRegisterRequest {

	@Email
	@NotNull
	private String email;

	@NotBlank
	@Pattern(regexp = "^[\\w,\\. ]+$")
	@Size(min = 6, max = 32)
	private String displayName;

}
