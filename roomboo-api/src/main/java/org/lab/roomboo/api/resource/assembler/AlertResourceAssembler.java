package org.lab.roomboo.api.resource.assembler;

import org.lab.roomboo.api.controller.AlertController;
import org.lab.roomboo.api.resource.AlertResource;
import org.lab.roomboo.domain.model.Alert;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class AlertResourceAssembler extends ResourceAssemblerSupport<Alert, AlertResource> {

	public AlertResourceAssembler() {
		super(AlertController.class, AlertResource.class);
	}

	@Override
	public AlertResource toResource(Alert entity) {
		return new AlertResource(entity);
	}

}
