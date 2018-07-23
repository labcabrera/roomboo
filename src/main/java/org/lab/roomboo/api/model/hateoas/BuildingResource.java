package org.lab.roomboo.api.model.hateoas;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.BuildingController;
import org.lab.roomboo.domain.model.Building;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class BuildingResource extends ResourceSupport {

	private final Building building;

	public BuildingResource(Building building) {
		this.building = building;
		String id = building.getId();
		add(linkTo(methodOn(BuildingController.class).findById(id)).withSelfRel());
		add(linkTo(BuildingController.class).withRel("buildings"));
	}

}
