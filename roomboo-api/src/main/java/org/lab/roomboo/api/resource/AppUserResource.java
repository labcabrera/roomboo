package org.lab.roomboo.api.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class AppUserResource extends ResourceSupport {

	private final AppUser appUser;

	public AppUserResource(AppUser appUser) {
		this.appUser = appUser;
		String id = appUser.getId();
		add(linkTo(methodOn(AppUserController.class).findById(id)).withSelfRel());
		add(linkTo(AppUserController.class).withRel("users"));
		add(linkTo(methodOn(ReserveController.class).find(StringUtils.EMPTY, id, false, false, 0, 10))
			.withRel("userReserves"));
	}

}
