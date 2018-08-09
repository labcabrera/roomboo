package org.lab.roomboo.core;

import org.lab.roomboo.domain.RoomboDomainConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.integration.annotation.IntegrationComponentScan;

@Configuration
@ComponentScan
@IntegrationComponentScan
@Import(RoomboDomainConfig.class)
public class RoombooCoreConfig {

}
