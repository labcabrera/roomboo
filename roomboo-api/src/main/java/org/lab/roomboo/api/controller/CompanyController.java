package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;
import java.util.stream.Collectors;

import org.lab.roomboo.api.model.hateoas.CompanyResource;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.repository.CompanyRepository;
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
@RequestMapping(value = "/v1/companies", produces = "application/hal+json")
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepository;

	@ApiOperation(value = "Company search")
	@GetMapping
	public ResponseEntity<Resources<CompanyResource>> find( // @formatter:off
			@RequestParam(value = "p", defaultValue = "0") Integer page,
			@RequestParam(value = "s", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		List<CompanyResource> collection = companyRepository.findAll(pageable).stream().map(CompanyResource::new)
			.collect(Collectors.toList());
		Resources<CompanyResource> resources = new Resources<>(collection);
		resources.add(new Link(ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString(), "self"));
		resources.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		resources.add(new Link(fromController(ReserveOwnerController.class).build().toString(), "owners"));
		return ResponseEntity.ok(resources);
	}

	@ApiOperation(value = "Company search by id")
	@GetMapping("/{id}")
	public ResponseEntity<CompanyResource> findById(@PathVariable("id") String id) {
		return companyRepository.findById(id).map(p -> ResponseEntity.ok(new CompanyResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(Company.class, id));
	}
}
