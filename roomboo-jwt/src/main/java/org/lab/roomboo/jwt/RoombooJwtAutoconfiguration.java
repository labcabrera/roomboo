package org.lab.roomboo.jwt;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(JwtSecurityConfig.class)
public class RoombooJwtAutoconfiguration {

}
