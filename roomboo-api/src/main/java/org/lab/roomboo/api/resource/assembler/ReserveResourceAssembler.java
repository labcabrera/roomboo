package org.lab.roomboo.api.resource.assembler;

import org.lab.roomboo.api.controller.ReserveController;
import org.lab.roomboo.api.resource.ReserveResource;
import org.lab.roomboo.domain.model.Reserve;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class ReserveResourceAssembler extends ResourceAssemblerSupport<Reserve, ReserveResource> {

	public ReserveResourceAssembler() {
		super(ReserveController.class, ReserveResource.class);
	}

	@Override
	public ReserveResource toResource(Reserve reserve) {
		return new ReserveResource(reserve);
	}

}
