package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resources.RoomResource;
import org.lab.roomboo.api.resources.RoomStatusResource;
import org.lab.roomboo.core.service.ReserveService;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.model.RoomGroup;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/rooms", produces = "application/hal+json")
public class RoomController {

	@Autowired
	private RoomRepository repository;

	@Autowired
	private ReserveService reserveService;

	@ApiOperation(value = "Room search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<Resources<RoomResource>> find( // @formatter:off
			@RequestParam(value = "groupId", required = false) String groupId,
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);

		Room exampleEntity = new Room();
		if (StringUtils.isNotBlank(groupId)) {
			exampleEntity.setGroup(RoomGroup.builder().id(groupId).build());
		}
		Example<Room> example = Example.of(exampleEntity);

		List<RoomResource> collection = repository.findAll(example, pageable).stream().map(RoomResource::new)
			.collect(Collectors.toList());
		Resources<RoomResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(AppUserController.class).build().toString(), "owners"));
		resources.add(new Link(fromController(RoomGroupController.class).build().toString(), "roomGroups"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Room search by id", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<RoomResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new RoomResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(Room.class, id));
	}

	@ApiOperation(value = "Room status", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}/status")
	public ResponseEntity<RoomStatusResource> status(@PathVariable("id") String id) {
		LocalDateTime now = LocalDateTime.now();
		Room room = repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Room.class, id));
		Reserve current = reserveService.findAtDateTime(id, now).orElse(null);
		Reserve next = reserveService.findNext(id, now).orElse(null);
		return ResponseEntity.ok(new RoomStatusResource(room, current, next));
	}

	@ApiOperation(value = "Room insert", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PostMapping
	public ResponseEntity<RoomResource> insert(@RequestBody Room entity) {
		Room inserted = repository.save(entity);
		URI uri = fromController(getClass()).path("/{id}").buildAndExpand(inserted.getId()).toUri();
		return ResponseEntity.created(uri).body(new RoomResource(inserted));
	}

	@ApiOperation(value = "Room update", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@PutMapping("/{id}")
	public ResponseEntity<RoomResource> update(@PathVariable("id") String id, @RequestBody Room entity) {
		entity.setId(id);
		Room inserted = repository.save(entity);
		RoomResource resource = new RoomResource(inserted);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(uri).body(resource);
	}

	@ApiOperation(value = "Room delete", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> {
			repository.deleteById(id);
			return ResponseEntity.noContent().build();
		}).orElseThrow(() -> new EntityNotFoundException(Room.class, id));
	}

}
