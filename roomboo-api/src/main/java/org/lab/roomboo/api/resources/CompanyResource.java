package org.lab.roomboo.api.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.BuildingController;
import org.lab.roomboo.api.controller.CompanyController;
import org.lab.roomboo.domain.model.Company;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class CompanyResource extends ResourceSupport {

	private final Company company;

	public CompanyResource(Company company) {
		this.company = company;
		String id = company.getId();
		add(linkTo(methodOn(CompanyController.class).findById(id)).withSelfRel());
		add(linkTo(methodOn(BuildingController.class).find(id, 0, 10)).withRel("buildings"));
		add(linkTo(CompanyController.class).withRel("companies"));
	}

}
