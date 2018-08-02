package org.lab.roomboo.api;

import org.lab.roomboo.api.bootstrap.RoombooInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RoombooApplication implements CommandLineRunner {

	@Autowired
	private RoombooInitializer initializer;

	public static void main(String[] args) {
		SpringApplication.run(RoombooApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initializer.checkDataInitialization();
	}
}
