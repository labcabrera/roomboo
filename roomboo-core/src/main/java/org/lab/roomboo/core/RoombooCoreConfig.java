package org.lab.roomboo.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.integration.annotation.IntegrationComponentScan;

@Configuration
@IntegrationComponentScan
@ComponentScan({ "org.lab.roomboo.core"}) //, "org.lab.roomboo.domain.repository" })
@EnableMongoRepositories("org.lab.roomboo.domain.repository")
public class RoombooCoreConfig {

}
