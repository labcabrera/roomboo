package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.model.hateoas.ReserveResource;
import org.lab.roomboo.domain.exception.BuildingNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ReserveRepository;
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

@RestController
@RequestMapping(value = "/v1/reserves", produces = "application/hal+json")
public class ReserveController {

	@Autowired
	private ReserveRepository repository;

	@ApiOperation(value = "Reserve search")
	@GetMapping
	public ResponseEntity<Resources<ReserveResource>> find( // @formatter:off
			@RequestParam(value = "roomId", required = false) String roomId,
			@RequestParam(value = "ownerId", required = false) String ownerId,
			@RequestParam(value = "p", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "s", defaultValue = "10", required = false) Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "from");
		Pageable pageable = PageRequest.of(page, size, sort);

		Reserve exampleEntity = new Reserve();
		if (StringUtils.isNotBlank(roomId)) {
			exampleEntity.setRoom(Room.builder().id(roomId).build());
		}
		if (StringUtils.isNotBlank(ownerId)) {
			exampleEntity.setOwner(ReserveOwner.builder().id(ownerId).build());
		}
		Example<Reserve> example = Example.of(exampleEntity);

		List<ReserveResource> collection = repository.findAll(example, pageable).stream().map(ReserveResource::new)
			.collect(Collectors.toList());
		Resources<ReserveResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(ReserveController.class).build().toString(), "reserves"));
		resources.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		resources.add(new Link(fromController(ReserveOwnerController.class).build().toString(), "owners"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Reserve search by id")
	@GetMapping("/{id}")
	public ResponseEntity<ReserveResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new ReserveResource(p)))
			.orElseThrow(() -> new BuildingNotFoundException(id));
	}
}
