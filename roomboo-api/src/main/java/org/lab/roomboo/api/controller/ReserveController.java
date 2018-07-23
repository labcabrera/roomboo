package org.lab.roomboo.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.lab.roomboo.api.model.hateoas.ReserveResource;
import org.lab.roomboo.domain.exception.BuildingNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
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
	private ReserveRepository reserveRepository;

	@ApiOperation(value = "Reserve search")
	@GetMapping
	public ResponseEntity<Resources<ReserveResource>> find( // @formatter:off
			@RequestParam(value = "roomId") String roomId,
			@RequestParam(value = "ownerId") String ownerId,
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "from");
		Pageable pageable = PageRequest.of(page, size, sort);

		Reserve exampleEntity = new Reserve();
		if (StringUtils.isNotBlank(roomId)) {
			exampleEntity.setRoom(Room.builder().id(roomId).build());
		}
		Example<Reserve> example = Example.of(exampleEntity);

		List<ReserveResource> collection = reserveRepository.findAll(example, pageable).stream()
			.map(ReserveResource::new).collect(Collectors.toList());
		Resources<ReserveResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Reserve search by id")
	@GetMapping("/{id}")
	public ResponseEntity<ReserveResource> findById(@PathVariable("id") String id) {
		return reserveRepository.findById(id).map(p -> ResponseEntity.ok(new ReserveResource(p)))
			.orElseThrow(() -> new BuildingNotFoundException(id));
	}
}
