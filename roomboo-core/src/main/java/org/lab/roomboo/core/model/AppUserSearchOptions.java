package org.lab.roomboo.core.model;

import org.lab.roomboo.core.validation.ValidAppUserRegisterRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ValidAppUserRegisterRequest
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserSearchOptions {

	private String name;
	
	private String email;

	private String companyId;

}
