package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.lab.roomboo.api.model.hateoas.RoomResource;
import org.lab.roomboo.domain.exception.RoomNotFoundException;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.RoomRepository;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/v1/rooms", produces = "application/hal+json")
public class RoomController {

	@Autowired
	private RoomRepository roomRepository;

	@ApiOperation(value = "Room search")
	@GetMapping
	public ResponseEntity<Resources<RoomResource>> find( // @formatter:off
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		List<RoomResource> collection = roomRepository.findAll(pageable).stream().map(RoomResource::new)
			.collect(Collectors.toList());
		Resources<RoomResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(ReserveOwnerController.class).build().toString(), "owners"));
		resources.add(new Link(fromController(BuildingController.class).build().toString(), "buildings"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Room search by id")
	@GetMapping("/{id}")
	public ResponseEntity<RoomResource> findById(@PathVariable("id") String id) {
		return roomRepository.findById(id).map(p -> ResponseEntity.ok(new RoomResource(p)))
			.orElseThrow(() -> new RoomNotFoundException(id));
	}

	@ApiOperation(value = "Room insert")
	@PostMapping
	public ResponseEntity<RoomResource> insert(@RequestBody Room entity) {
		Room inserted = roomRepository.save(entity);
		URI uri = fromController(getClass()).path("/{id}").buildAndExpand(inserted.getId()).toUri();
		return ResponseEntity.created(uri).body(new RoomResource(inserted));
	}

	@ApiOperation(value = "Room update")
	@PutMapping("/{id}")
	public ResponseEntity<RoomResource> update(@PathVariable("id") String id, @RequestBody Room entity) {
		entity.setId(id);
		Room inserted = roomRepository.save(entity);
		RoomResource resource = new RoomResource(inserted);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.created(uri).body(resource);
	}

	@ApiOperation(value = "Room delete")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable("id") String id) {
		return roomRepository.findById(id).map(p -> {
			roomRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}).orElseThrow(() -> new RoomNotFoundException(id));
	}

}
