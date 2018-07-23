package org.lab.roomboo.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.lab.roomboo.api.model.ReserveOwnerResource;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.exception.ReserveOwnerNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(value = "/v1/reserves", produces = "application/hal+json")
public class ReserveOwnerController {

	@Autowired
	private ReserveOwnerRepository personRepository;

	// TODO pagination
	@GetMapping
	public ResponseEntity<Resources<ReserveOwnerResource>> findAll() {
		List<ReserveOwnerResource> collection = personRepository.findAll().stream().map(ReserveOwnerResource::new)
			.collect(Collectors.toList());
		Resources<ReserveOwnerResource> resources = new Resources<>(collection);
		String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
		resources.add(new Link(uriString, "self"));
		return ResponseEntity.ok(resources);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ReserveOwnerResource> findById(@PathVariable("id") String id) {
		return personRepository.findById(id).map(p -> ResponseEntity.ok(new ReserveOwnerResource(p)))
			.orElseThrow(() -> new ReserveOwnerNotFound(id));
	}

	//
	// @PostMapping
	// public ResponseEntity<PersonResource> post(@RequestBody final Person personFromRequest) {
	//
	// final Person person = new Person(personFromRequest);
	//
	// personRepository.save(person);
	//
	// final URI uri = MvcUriComponentsBuilder.fromController(getClass()).path("/{id}").buildAndExpand(person.getId())
	// .toUri();
	//
	// return ResponseEntity.created(uri).body(new PersonResource(person));
	//
	// }
	//
	// @PutMapping("/{id}")
	// public ResponseEntity<PersonResource> put(@PathVariable("id") final long id,
	// @RequestBody Person personFromRequest) {
	//
	// final Person person = new Person(personFromRequest, id);
	//
	// personRepository.save(person);
	//
	// final PersonResource resource = new PersonResource(person);
	//
	// final URI uri = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
	//
	// return ResponseEntity.created(uri).body(resource);
	//
	// }
	//
	// @DeleteMapping("/{id}") public ResponseEntity << ? > delete(@PathVariable("id") final long id) {
	//
	// return personRepository.findById(id).map(p - > {
	//
	// personRepository.deleteById(id);
	//
	// return ResponseEntity.noContent().build();
	//
	// }).orElseThrow(() - > new PersonNotFoundException(id));
	//
	// }

}
