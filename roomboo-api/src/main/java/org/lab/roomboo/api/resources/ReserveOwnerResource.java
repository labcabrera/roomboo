package org.lab.roomboo.api.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.api.controller.ReserveOwnerController;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ReserveOwnerResource extends ResourceSupport {

	private final ReserveOwner reserveOwner;

	public ReserveOwnerResource(ReserveOwner reserveOwner) {
		this.reserveOwner = reserveOwner;
		String id = reserveOwner.getId();
		add(linkTo(methodOn(ReserveOwnerController.class).findById(id)).withSelfRel());
		add(linkTo(ReserveOwnerController.class).withRel("owners"));
		add(linkTo(methodOn(ReserveController.class).find(StringUtils.EMPTY, id, 0, 10)).withRel("reserves"));
	}

}
