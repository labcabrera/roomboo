package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.net.URI;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.AppUserResource;
import org.lab.roomboo.api.resource.assembler.AppUserResourceAssembler;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.AppUser;
import org.lab.roomboo.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/users", produces = "application/hal+json")
public class AppUserController {

	@Autowired
	private AppUserRepository repository;

	@Autowired
	private AppUserResourceAssembler appUserResourceAssembler;

	@Autowired
	private PagedResourcesAssembler<AppUser> assembler;

	@ApiOperation(value = "App user search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<Resources<AppUserResource>> findAll( //@formatter:off
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) { //@formatter:on
		Sort sort = new Sort(Sort.Direction.ASC, "displayName");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<AppUser> currentPage = repository.findAll(pageable);
		PagedResources<AppUserResource> pr = assembler.toResource(currentPage, appUserResourceAssembler);
		pr.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		return ResponseEntity.ok(pr);
	}

	@ApiOperation(value = "App user find by id",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<AppUserResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new AppUserResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
	}

	@ApiOperation(value = "App user insert", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping
	public ResponseEntity<AppUserResource> save(@RequestBody AppUser entity) {
		AppUser inserted = repository.save(entity);
		URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}").buildAndExpand(inserted.getId())
			.toUri();
		return ResponseEntity.created(uri).body(new AppUserResource(inserted));
	}

	@ApiOperation(value = "App user update", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PutMapping("/{id}")
	public ResponseEntity<AppUserResource> update(@PathVariable("id") String id, @RequestBody AppUser entity) {
		entity.setId(id);
		repository.save(entity);
		AppUserResource resource = new AppUserResource(entity);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(uri).body(resource);
	}

	@ApiOperation(value = "App user delete", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}).orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
	}

}
