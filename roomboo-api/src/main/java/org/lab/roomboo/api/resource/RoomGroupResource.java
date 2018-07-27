package org.lab.roomboo.api.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.CompanyController;
import org.lab.roomboo.api.controller.RoomController;
import org.lab.roomboo.api.controller.RoomGroupController;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.RoomGroup;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class RoomGroupResource extends ResourceSupport {

	private final RoomGroup roomGroup;

	public RoomGroupResource(RoomGroup roomGroup) {
		this.roomGroup = roomGroup;
		String id = roomGroup.getId();
		add(linkTo(methodOn(RoomGroupController.class).findById(id)).withSelfRel());
		if (roomGroup.getCompany() != null) {
			String companyId = roomGroup.getCompany().getId();
			add(linkTo(methodOn(CompanyController.class).findById(companyId)).withRel("company"));
			roomGroup.setCompany(Company.builder().id(companyId).build());
		}
		add(linkTo(methodOn(RoomController.class).find(id, 0, 10)).withRel("rooms"));
		add(linkTo(RoomGroupController.class).withRel("roomGroups"));
	}

}
