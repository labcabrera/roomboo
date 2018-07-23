package org.lab.roomboo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("org.lab.roomboo.repository")
public class RoombooConfiguration {

}
