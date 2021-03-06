package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.net.URI;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.query.BasicQueryDsl;
import org.lab.roomboo.api.resource.AppUserResource;
import org.lab.roomboo.core.model.AppUserSearchOptions;
import org.lab.roomboo.core.service.AppUserService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/users", produces = "application/hal+json")
public class AppUserController {

	@Autowired
	private AppUserService userService;

	@Autowired
	private AppUserRepository repository;

	@Autowired
	private PagedResourcesAssembler<AppUser> assembler;

	@ApiOperation(value = "App user search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<Resources<AppUserResource>> find( // @formatter:off
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.ASC, "displayName");
		Pageable pageable = PageRequest.of(page, size, sort);

		BasicQueryDsl queryDsl = new BasicQueryDsl(query);
		AppUserSearchOptions searchOptions = AppUserSearchOptions.builder().text(queryDsl.get("text"))
				.email(queryDsl.get("mail")).companyId(queryDsl.get("companyId")).build();

		Page<AppUser> currentPage = userService.findPageable(searchOptions, pageable);
		PagedResources<AppUserResource> pr = assembler.toResource(currentPage, e -> new AppUserResource(e));
		pr.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		pr.add(new Link(fromController(CompanyController.class).build().toString(), "companies"));
		return ResponseEntity.ok(pr);
	}

	@ApiOperation(value = "App user find by id", authorizations = {
			@Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<AppUserResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new AppUserResource(p)))
				.orElseThrow(() -> new EntityNotFoundException(AppUser.class, id));
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
