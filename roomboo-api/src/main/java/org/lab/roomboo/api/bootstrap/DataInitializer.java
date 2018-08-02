package org.lab.roomboo.api.bootstrap;

import java.io.InputStream;
import java.util.List;

import org.lab.roomboo.domain.exception.RoombooException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class DataInitializer<T> {

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	protected final MongoRepository<T, String> repository;

	@Value("${app.env.initialization.force:false}")
	protected boolean forceInitialization;

	protected final Class<T> type;

	protected final String resourcePath;

	public DataInitializer(Class<T> type, MongoRepository<T, String> repository, String resourcePath) {
		this.type = type;
		this.repository = repository;
		this.resourcePath = resourcePath;
	}

	public boolean isInitialized() {
		return !(forceInitialization || repository.count() == 0);
	}

	public void initialize() {
		repository.deleteAll();
		List<T> entities = readEntities();
		repository.insert(entities);
		log.info("Inserted {} {} entities", entities.size(), type.getSimpleName());
	}

	protected List<T> readEntities() {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, type);
		try (InputStream in = classLoader.getResourceAsStream(resourcePath)) {
			List<T> list = objectMapper.readValue(in, javaType);
			return list;
		}
		catch (Exception ex) {
			throw new RoombooException("Error reading " + resourcePath + " as JSON list of " + type.getName(), ex);
		}
	}

	public static interface InitializationOrder {
		int ApiUser = 2000;
		int Company = 2010;
		int RoomGroup = 2020;
		int Room = 2030;
		int ReserveOwner = 2040;
		int Reserve = 2050;
	}

}
