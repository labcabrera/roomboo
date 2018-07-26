package org.lab.roomboo.api.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.RoomGroupController;
import org.lab.roomboo.api.controller.CompanyController;
import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.domain.model.RoomGroup;
import org.lab.roomboo.domain.model.Company;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class BuildingResource extends ResourceSupport {

	private final RoomGroup building;

	public BuildingResource(RoomGroup building) {
		this.building = building;
		String id = building.getId();
		add(linkTo(methodOn(RoomGroupController.class).findById(id)).withSelfRel());
		if (building.getCompany() != null) {
			String companyId = building.getCompany().getId();
			add(linkTo(methodOn(CompanyController.class).findById(companyId)).withRel("company"));
			building.setCompany(Company.builder().id(companyId).build());
		}
		add(linkTo(methodOn(RoomController.class).find(id, 0, 10)).withRel("rooms"));
		add(linkTo(RoomGroupController.class).withRel("buildings"));
	}

}
