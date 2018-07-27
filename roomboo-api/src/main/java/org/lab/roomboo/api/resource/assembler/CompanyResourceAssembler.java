package org.lab.roomboo.api.resource.assembler;

import org.lab.roomboo.api.controller.CompanyController;
import org.lab.roomboo.api.resource.CompanyResource;
import org.lab.roomboo.domain.model.Company;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class CompanyResourceAssembler extends ResourceAssemblerSupport<Company, CompanyResource> {

	public CompanyResourceAssembler() {
		super(CompanyController.class, CompanyResource.class);
	}

	@Override
	public CompanyResource toResource(Company entity) {
		return new CompanyResource(entity);
	}

}
