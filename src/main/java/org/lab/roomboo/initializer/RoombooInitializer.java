package org.lab.roomboo.initializer;

import java.io.InputStream;
import java.util.List;

import org.lab.roomboo.exception.RoombooException;
import org.lab.roomboo.model.Building;
import org.lab.roomboo.model.Company;
import org.lab.roomboo.model.Room;
import org.lab.roomboo.repository.BuildingRepository;
import org.lab.roomboo.repository.CompanyRepository;
import org.lab.roomboo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RoombooInitializer {

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private BuildingRepository buildingRepository;

	@Autowired
	private RoomRepository roomRepository;

	public boolean isInitialized() {
		return false;
	}

	public void initialize() {
		log.info("Loading initialization data");

		buildingRepository.deleteAll();
		companyRepository.deleteAll();
		roomRepository.deleteAll();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/companies.json")) {
			List<Company> list = objectMapper.readValue(in, new TypeReference<List<Company>>() {
			});
			companyRepository.insert(list);
			log.info("Inserted {} companies", list.size());
		} catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/buildings.json")) {
			List<Building> list = objectMapper.readValue(in, new TypeReference<List<Building>>() {
			});
			buildingRepository.insert(list);
			log.info("Inserted {} buildings", list.size());
		} catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/rooms.json")) {
			List<Room> list = objectMapper.readValue(in, new TypeReference<List<Room>>() {
			});
			roomRepository.insert(list);
			log.info("Inserted {} rooms", list.size());
		} catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}
	}
}
