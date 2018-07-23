package org.lab.roomboo.service;

import java.util.List;

import org.lab.roomboo.api.model.RoomSearchRequest;
import org.lab.roomboo.model.Room;
import org.lab.roomboo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoomSearchService {

	@Autowired
	private RoomRepository repository;

	public List<Room> search() {
		return repository.findAll();
	}

	public Page<Room> search(RoomSearchRequest request, Pageable pageable) {
		return repository.search(request.getBuildingId(), pageable);
	}

}
