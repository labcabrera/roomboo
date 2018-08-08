package org.lab.roomboo.api.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.controller.CompanyController;
import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.model.Company;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class AppUserResource extends ResourceSupport {

	private final AppUser appUser;

	public AppUserResource(AppUser appUser) {
		this.appUser = appUser;
		String id = appUser.getId();
		String companyId = appUser.getCompany().getId();
		appUser.setCompany(Company.builder().id(companyId).build());

		add(linkTo(methodOn(AppUserController.class).findById(id)).withSelfRel());
		add(linkTo(methodOn(CompanyController.class).findById(companyId)).withRel("company"));
		add(linkTo(AppUserController.class).withRel("users"));
		add(linkTo(methodOn(ReserveController.class).find("userId==" + id, 0, 10)).withRel("userReserves"));
	}

}
