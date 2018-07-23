package org.lab.roomboo.api.controller;

import org.lab.roomboo.api.model.RoomReserveRequest;
import org.lab.roomboo.api.service.ReserveService;
import org.lab.roomboo.domain.model.RoomReserve;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/v1/reserves")
@RestController
public class ReserveController {

	@Autowired
	private ReserveService reserveService;

	@ApiOperation(value = "Room reserve search")
	@PostMapping
	private Page<RoomReserve> find( // @formatter:off
		@RequestBody RoomReserveRequest request,
		@RequestParam(value = "p", defaultValue = "0") Integer page,
		@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "from");
		Pageable pageable = PageRequest.of(page, size, sort);
		return reserveService.search(request, pageable);
	}

	@ApiOperation(value = "Room reserve request")
	public ResponseEntity<RoomReserve> reserve(@RequestBody RoomReserveRequest request) {
		RoomReserve entity = reserveService.reserve(request);
		return ResponseEntity.ok(entity);
	}

}
