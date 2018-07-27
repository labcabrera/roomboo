package org.lab.roomboo.api.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReserveOwnerResource extends ResourceSupport {

	private final AppUser reserveOwner;

	public ReserveOwnerResource(AppUser reserveOwner) {
		this.reserveOwner = reserveOwner;
		String id = reserveOwner.getId();
		add(linkTo(methodOn(AppUserController.class).findById(id)).withSelfRel());
		add(linkTo(AppUserController.class).withRel("owners"));
		add(linkTo(methodOn(ReserveController.class).find(StringUtils.EMPTY, id, false, false, 0, 10))
			.withRel("reserves"));
	}

}
