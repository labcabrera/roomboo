package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;
import java.util.stream.Collectors;

import org.lab.roomboo.api.model.hateoas.BuildingResource;
import org.lab.roomboo.domain.exception.BuildingNotFoundException;
import org.lab.roomboo.domain.repository.BuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/v1/buildings", produces = "application/hal+json")
public class BuildingController {

	@Autowired
	private BuildingRepository buildingRepository;

	@ApiOperation(value = "Room search")
	@GetMapping
	public ResponseEntity<Resources<BuildingResource>> find( // @formatter:off
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		List<BuildingResource> collection = buildingRepository.findAll(pageable).stream().map(BuildingResource::new)
			.collect(Collectors.toList());
		Resources<BuildingResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		resources.add(new Link(fromController(ReserveOwnerController.class).build().toString(), "owners"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Building search by id")
	@GetMapping("/{id}")
	public ResponseEntity<BuildingResource> findById(@PathVariable("id") String id) {
		return buildingRepository.findById(id).map(p -> ResponseEntity.ok(new BuildingResource(p)))
			.orElseThrow(() -> new BuildingNotFoundException(id));
	}

}
