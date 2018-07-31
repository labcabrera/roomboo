package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.net.URI;
import java.time.LocalDateTime;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.query.BasicQueryDsl;
import org.lab.roomboo.api.resource.RoomResource;
import org.lab.roomboo.api.resource.RoomStatusResource;
import org.lab.roomboo.api.resource.assembler.RoomResourceAssembler;
import org.lab.roomboo.core.model.RoomSearchOptions;
import org.lab.roomboo.core.service.ReserveService;
import org.lab.roomboo.core.service.RoomService;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
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
	private RoomService roomService;

	@Autowired
	private RoomRepository repository;

	@Autowired
	private ReserveService reserveService;

	@Autowired
	private RoomResourceAssembler roomResourceAssembler;

	@Autowired
	private PagedResourcesAssembler<Room> assembler;

	@ApiOperation(value = "Room search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<PagedResources<RoomResource>> find( // @formatter:off
			@RequestParam(value = "q", required = false) String query,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size
			) { // @formatter:on
		BasicQueryDsl queryDsl = new BasicQueryDsl(query);
		Sort sort = new Sort(Sort.Direction.DESC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		RoomSearchOptions options = RoomSearchOptions.builder().groupId(queryDsl.get("groupId")).build();
		Page<Room> currentPage = roomService.findPageable(options, pageable);
		PagedResources<RoomResource> pr = assembler.toResource(currentPage, roomResourceAssembler);
		pr.add(new Link(fromController(RoomGroupController.class).build().toString(), "roomGroups"));
		return ResponseEntity.ok(pr);
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
