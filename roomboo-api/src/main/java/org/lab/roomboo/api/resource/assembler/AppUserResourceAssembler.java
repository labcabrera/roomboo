package org.lab.roomboo.api.resource.assembler;

import org.lab.roomboo.api.controller.AppUserController;
import org.lab.roomboo.api.resource.AppUserResource;
import org.lab.roomboo.domain.model.AppUser;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class AppUserResourceAssembler extends ResourceAssemblerSupport<AppUser, AppUserResource> {

	public AppUserResourceAssembler() {
		super(AppUserController.class, AppUserResource.class);
	}

	@Override
	public AppUserResource toResource(AppUser entity) {
		return new AppUserResource(entity);
	}

}
