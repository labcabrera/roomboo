package org.lab.roomboo.api.model;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
		add(linkTo(ReserveOwnerController.class).withRel("owners"));
		add(linkTo(methodOn(ReserveOwnerController.class).findById(id)).withSelfRel());
	}

}
