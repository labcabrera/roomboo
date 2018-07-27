package org.lab.roomboo.api.controller;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.lab.roomboo.api.config.SwaggerConfig;
import org.lab.roomboo.api.resource.CompanyResource;
import org.lab.roomboo.api.resource.assembler.CompanyResourceAssembler;
import org.lab.roomboo.domain.exception.EntityNotFoundException;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.repository.CompanyRepository;
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
@RequestMapping(value = "/v1/companies", produces = "application/hal+json")
public class CompanyController {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private CompanyResourceAssembler companyResourceAssembler;

	@Autowired
	private PagedResourcesAssembler<Company> assembler;

	@ApiOperation(value = "Company search", authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping
	public ResponseEntity<PagedResources<CompanyResource>> find( // @formatter:off
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size) { // @formatter:on
		Sort sort = new Sort(Sort.Direction.ASC, "name");
		Pageable pageable = PageRequest.of(page, size, sort);
		Page<Company> currentPage = companyRepository.findAll(pageable);
		PagedResources<CompanyResource> pr = assembler.toResource(currentPage, companyResourceAssembler);
		pr.add(new Link(fromController(RoomController.class).build().toString(), "rooms"));
		pr.add(new Link(fromController(AppUserController.class).build().toString(), "owners"));
		return ResponseEntity.ok(pr);
	}

	@ApiOperation(value = "Company search by id",
		authorizations = { @Authorization(value = SwaggerConfig.API_KEY_NAME) })
	@GetMapping("/{id}")
	public ResponseEntity<CompanyResource> findById(@PathVariable("id") String id) {
		return companyRepository.findById(id).map(p -> ResponseEntity.ok(new CompanyResource(p)))
			.orElseThrow(() -> new EntityNotFoundException(Company.class, id));
	}
}
