package org.lab.roomboo.api.controller;

import java.util.List;

import org.lab.roomboo.api.model.RoomSearchRequest;
import org.lab.roomboo.model.Room;
import org.lab.roomboo.service.RoomSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/rooms")
@RestController
public class RoomController {

	@Autowired
	private RoomSearchService searchService;

	@GetMapping
	public List<Room> get() {
		return searchService.search();
	}

	@PostMapping
	private Page<Room> search( // @formatter:off
			@RequestBody RoomSearchRequest request,
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		return searchService.search(request, pageable);
	}

}
