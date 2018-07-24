package org.lab.roomboo.api.bootstrap;

import java.io.InputStream;
import java.util.List;

import org.lab.roomboo.domain.exception.RoombooException;
import org.lab.roomboo.domain.model.ApiUser;
import org.lab.roomboo.domain.model.Building;
import org.lab.roomboo.domain.model.Company;
import org.lab.roomboo.domain.model.ReserveOwner;
import org.lab.roomboo.domain.model.Room;
import org.lab.roomboo.domain.repository.ApiUserRepository;
import org.lab.roomboo.domain.repository.BuildingRepository;
import org.lab.roomboo.domain.repository.CompanyRepository;
import org.lab.roomboo.domain.repository.ReserveOwnerRepository;
import org.lab.roomboo.domain.repository.ReserveRepository;
import org.lab.roomboo.domain.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

	@Autowired
	private ReserveOwnerRepository reserveOwnerRepository;

	@Autowired
	private ReserveRepository roomReserveRepository;

	@Autowired
	private ApiUserRepository apiUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public boolean isInitialized() {
		// TODO force restart data
		return false;
	}

	// TODO refactor
	public void initialize() {
		log.info("Loading initialization data");

		apiUserRepository.deleteAll();
		roomReserveRepository.deleteAll();
		reserveOwnerRepository.deleteAll();
		buildingRepository.deleteAll();
		companyRepository.deleteAll();
		roomRepository.deleteAll();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/companies.json")) {
			List<Company> list = objectMapper.readValue(in, new TypeReference<List<Company>>() {
			});
			companyRepository.insert(list);
			log.info("Inserted {} companies", list.size());
		}
		catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/buildings.json")) {
			List<Building> list = objectMapper.readValue(in, new TypeReference<List<Building>>() {
			});
			buildingRepository.insert(list);
			log.info("Inserted {} buildings", list.size());
		}
		catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/rooms.json")) {
			List<Room> list = objectMapper.readValue(in, new TypeReference<List<Room>>() {
			});
			roomRepository.insert(list);
			log.info("Inserted {} rooms", list.size());
		}
		catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/owners.json")) {
			List<ReserveOwner> list = objectMapper.readValue(in, new TypeReference<List<ReserveOwner>>() {
			});
			reserveOwnerRepository.insert(list);
			log.info("Inserted {} reserve owners", list.size());
		}
		catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}

		try (InputStream in = classLoader.getResourceAsStream("bootstrap/users.json")) {
			List<ApiUser> list = objectMapper.readValue(in, new TypeReference<List<ApiUser>>() {
			});
			list.forEach(x -> {
				x.setPassword(passwordEncoder.encode(x.getPassword()));
			});
			apiUserRepository.insert(list);
			log.info("Inserted {} users", list.size());
		}
		catch (Exception ex) {
			throw new RoombooException("Bootstrap error", ex);
		}

	}
}
