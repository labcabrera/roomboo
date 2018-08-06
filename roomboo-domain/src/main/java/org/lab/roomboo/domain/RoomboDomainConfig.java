package org.lab.roomboo.domain;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("org.lab.roomboo.domain.repository")
public class RoomboDomainConfig {

}
