package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.AlertResource;
import org.lab.roomboo.api.resource.assembler.AlertResourceAssembler;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Alert;
import org.lab.roomboo.domain.repository.AlertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/alerts", produces = "application/hal+json")
public class AlertController {

	@Autowired
	private AlertRepository repository;

	@Autowired
	private AlertResourceAssembler alertResourceAssembler;

	@Autowired
	private PagedResourcesAssembler<Alert> assembler;

	@ApiOperation(value = "Alert search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<PagedResources<AlertResource>> find( // @formatter:off
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "created");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Alert> currentPage = repository.findAll(pageable);
		PagedResources<AlertResource> pr = assembler.toResource(currentPage, alertResourceAssembler);
		pr.add(new Link(fromController(AppUserController.class).build().toString(), "users"));
		return ResponseEntity.ok(pr);
	}

	@ApiOperation(value = "Alert search by id", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<AlertResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new AlertResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(Alert.class, id));
	}
}
