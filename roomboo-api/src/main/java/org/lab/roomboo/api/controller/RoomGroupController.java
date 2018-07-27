package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.RoomGroupResource;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.RoomGroup;
import org.lab.roomboo.domain.repository.RoomGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
@RequestMapping(value = "/v1/roomGroups", produces = "application/hal+json")
public class RoomGroupController {

	@Autowired
	private RoomGroupRepository repository;

	@ApiOperation(value = "Room group search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<Resources<RoomGroupResource>> find( // @formatter:off
			@RequestParam(value = "companyId", required = false) String companyId,
			@RequestParam(value = "p", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "s", defaultValue = "10", required = false) Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);

		RoomGroup exampleEntity = new RoomGroup();
		if (StringUtils.isNotBlank(companyId)) {
			exampleEntity.setCompany(Company.builder().id(companyId).build());
		}
		Example<RoomGroup> example = Example.of(exampleEntity);

		List<RoomGroupResource> collection = repository.findAll(example, pageable).stream()
			.map(RoomGroupResource::new).collect(Collectors.toList());
		Resources<RoomGroupResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		resources.add(new Link(fromController(AppUserController.class).build().toString(), "owners"));
		resources.add(new Link(fromController(CompanyController.class).build().toString(), "companies"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Room group search by id",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<RoomGroupResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new RoomGroupResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(RoomGroup.class, id));
	}

}
