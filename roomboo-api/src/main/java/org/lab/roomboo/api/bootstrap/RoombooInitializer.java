package org.lab.roomboo.api.bootstrap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class RoombooInitializer {

	@Autowired
	private List<DataInitializer<?>> initializers;

	public void checkDataInitialization() {
		log.info("Loading initialization data");
		initializers.stream().filter(x -> !x.isInitialized()).forEach(x -> x.initialize());
	}
}
