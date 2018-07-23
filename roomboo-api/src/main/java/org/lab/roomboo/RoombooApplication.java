package org.lab.roomboo;

import org.lab.roomboo.domain.initializer.RoombooInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class RoombooApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RoombooApplication.class, args);
	}

	@Autowired
	private RoombooInitializer initializer;

	@Override
	public void run(String... args) throws Exception {
		if (!initializer.isInitialized()) {
			initializer.initialize();
		}
	}
}
