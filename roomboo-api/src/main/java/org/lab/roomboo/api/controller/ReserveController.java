package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.ReserveResource;
import org.lab.roomboo.api.resource.assembler.ReserveResourceAssembler;
import org.lab.roomboo.core.model.ReserveSearchOptions;
import org.lab.roomboo.core.service.ReserveService;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/reserves", produces = "application/hal+json")
public class ReserveController {

	@Autowired
	private ReserveRepository repository;

	@Autowired
	private ReserveService reserveService;

	@Autowired
	private ReserveResourceAssembler reserveResourceAssembler;

	@Autowired
	private PagedResourcesAssembler<Reserve> assembler;

	@ApiOperation(value = "Reserve search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<PagedResources<ReserveResource>> find( // @formatter:off
			@RequestParam(value = "roomId", required = false) String roomId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "includeUnconfirmed", required = false, defaultValue = "false") boolean includeUnconfirmed,
			@RequestParam(value = "includeCancelled", required = false, defaultValue = "false") boolean includeCancelled,
			@RequestParam(value = "page", defaultValue = "0", required = false) int page,
			@RequestParam(value = "size", defaultValue = "10", required = false) int size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.DESC, "from");
		Pageable pageable = PageRequest.of(page, size, sort);
		ReserveSearchOptions options = ReserveSearchOptions.builder().roomId(roomId).userId(userId)
			.includeUnconfirmed(includeUnconfirmed).includeCancelled(includeCancelled).build();
		Page<Reserve> currentPage = reserveService.findPaginated(options, pageable);
		PagedResources<ReserveResource> pr = assembler.toResource(currentPage, reserveResourceAssembler);
		pr.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		pr.add(new Link(fromController(AppUserController.class).build().toString(), "owners"));
		return ResponseEntity.ok(pr);
	}

	@ApiOperation(value = "Reserve search by id",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<ReserveResource> findById(@PathVariable("id") String id) {
		return repository.findById(id).map(p -> ResponseEntity.ok(new ReserveResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(Reserve.class, id));
	}

}
