package org.lab.roomboo.api.controller;

import java.util.Optional;

import org.lab.roomboo.api.model.RoomSearchRequest;
import org.lab.roomboo.model.Room;
import org.lab.roomboo.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/v1/rooms")
@RestController
public class RoomController {

	@Autowired
	private RoomService roomService;

	@ApiOperation(value = "Room search by id")
	@GetMapping("/{id}")
	private ResponseEntity<Room> findById(@PathVariable("id") String id) {
		Optional<Room> optional = roomService.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		return new ResponseEntity<Room>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Room insert")
	@PutMapping
	private Room insert(@RequestBody Room entity) {
		return roomService.insert(entity);
	}

	@ApiOperation(value = "Room update")
	@PatchMapping
	private Room update(@RequestBody Room entity) {
		return roomService.update(entity);

	}

	@ApiOperation(value = "Room delete")
	@DeleteMapping("/{id}")
	private ResponseEntity<Room> delete(@PathVariable("id") String id) {
		Optional<Room> optional = roomService.findById(id);
		if (optional.isPresent()) {
			return ResponseEntity.ok(optional.get());
		}
		return new ResponseEntity<Room>(HttpStatus.NO_CONTENT);
	}

	@ApiOperation(value = "Room search")
	@PostMapping
	private Page<Room> find( // @formatter:off
			@RequestBody RoomSearchRequest request,
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		return roomService.search(request, pageable);
	}

}
