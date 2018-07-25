package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;
import java.util.stream.Collectors;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resources.AlertResource;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/alerts", produces = "application/hal+json")
public class AlertController {

	@Autowired
	private AlertRepository repository;

	@ApiOperation(value = "Alert search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<Resources<AlertResource>> find( // @formatter:off
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		List<AlertResource> collection = repository.findAll(pageable).stream().map(AlertResource::new)
			.collect(Collectors.toList());
		Resources<AlertResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		resources.add(new Link(fromController(ReserveOwnerController.class).build().toString(), "owners"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Alert search by id", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<AlertResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new AlertResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(Alert.class, id));
	}
}