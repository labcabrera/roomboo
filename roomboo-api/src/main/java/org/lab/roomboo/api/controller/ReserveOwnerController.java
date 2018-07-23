package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.lab.roomboo.api.model.hateoas.ReserveOwnerResource;
import org.lab.roomboo.domain.exception.ReserveOwnerNotFoundException;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
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

@RestController
@RequestMapping(value = "/v1/owners", produces = "application/hal+json")
public class ReserveOwnerController {

	@Autowired
	private ReserveOwnerRepository reserveOwnerRepository;

	// TODO pagination
	@ApiOperation(value = "Reserve owner search")
	@GetMapping
	public ResponseEntity<Resources<ReserveOwnerResource>> findAll( //@formatter:off
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { //@formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		List<ReserveOwnerResource> collection = reserveOwnerRepository.findAll(pageable).stream()
			.map(ReserveOwnerResource::new).collect(Collectors.toList());
		Resources<ReserveOwnerResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		resources.add(new Link(fromController(BuildingController.class).build().toString(), "buildings"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Reserve owner find by id")
	@GetMapping("/{id}")
	public ResponseEntity<ReserveOwnerResource> findById(@PathVariable("id") String id) {
		return reserveOwnerRepository.findById(id).map(p -> ResponseEntity.ok(new ReserveOwnerResource(p)))
			.orElseThrow(() -> new ReserveOwnerNotFoundException(id));
	}

	@ApiOperation(value = "Reserve owner insert")
	@PostMapping
	public ResponseEntity<ReserveOwnerResource> save(@RequestBody ReserveOwner entity) {
		ReserveOwner inserted = reserveOwnerRepository.save(entity);
		URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}").buildAndExpand(inserted.getId())
			.toUri();
		return ResponseEntity.created(uri).body(new ReserveOwnerResource(inserted));
	}

	@ApiOperation(value = "Reserve owner update")
	@PutMapping("/{id}")
	public ResponseEntity<ReserveOwnerResource> update(@PathVariable("id") String id,
		@RequestBody ReserveOwner entity) {
		entity.setId(id);
		reserveOwnerRepository.save(entity);
		ReserveOwnerResource resource = new ReserveOwnerResource(entity);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(uri).body(resource);
	}

	@ApiOperation(value = "Reserve owner delete")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return reserveOwnerRepository.findById(id).map(p -> {
			reserveOwnerRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}).orElseThrow(() -> new ReserveOwnerNotFoundException(id));
	}

}
