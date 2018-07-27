package org.lab.roomboo.api.resource;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.lab.roomboo.api.controller.AlertController;
import org.lab.roomboo.domain.model.Alert;
import org.springframework.hateoas.ResourceSupport;

import lombok.Getter;

@Getter
public class AlertResource extends ResourceSupport {

	private final Alert alert;

	public AlertResource(Alert alert) {
		this.alert = alert;
		String id = alert.getId();
		add(linkTo(methodOn(AlertController.class).findById(id)).withSelfRel());
		add(linkTo(AlertController.class).withRel("alerts"));
	}

}
