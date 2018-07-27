package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.ReserveResource;
import org.lab.roomboo.api.resource.assembler.ReserveResourceAssembler;
import org.lab.roomboo.core.service.ReserveService;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Reserve;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.PagedResources.PageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping(value = "/v1/reserves", produces = "application/hal+json")
public class ReserveController {

	@Autowired
	private ReserveRepository repository;

	@Autowired
	private ReserveService reserveService;

	@ApiOperation(value = "Reserve search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<PagedResources<ReserveResource>> find( // @formatter:off
			@RequestParam(value = "roomId", required = false) String roomId,
			@RequestParam(value = "userId", required = false) String userId,
			@RequestParam(value = "includeUnconfirmed", required = false, defaultValue = "false") boolean includeUnconfirmed,
			@RequestParam(value = "includeCancelled", required = false, defaultValue = "false") boolean includeCancelled,
			@RequestParam(value = "p", defaultValue = "0", required = false) int page,
			@RequestParam(value = "s", defaultValue = "10", required = false) int size) { // @formatter:on

		Sort sort = new Sort(Sort.Direction.DESC, "from");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Reserve> currentPage = reserveService.findPaginated(roomId, userId, includeUnconfirmed, includeCancelled,
			pageable);
		List<ReserveResource> resources = new ReserveResourceAssembler().toResources(currentPage.getContent());
		PageMetadata md = new PageMetadata(size, page, currentPage.getTotalElements(), currentPage.getTotalPages());
		PagedResources<ReserveResource> pr = new PagedResources<>(resources, md);
		pr.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		pr.add(new Link(fromController(ReserveController.class).build().toString(), "reserves"));
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
