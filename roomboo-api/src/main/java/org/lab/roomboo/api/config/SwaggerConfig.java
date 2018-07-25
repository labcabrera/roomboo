package org.lab.roomboo.api.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		//@formatter:off
		return new Docket(DocumentationType.SWAGGER_2) 
			.select()
			.apis(RequestHandlerSelectors.basePackage("org.lab.roomboo"))
			.paths(PathSelectors.any())
			.build()
			.apiInfo(apiInfo())
			.securitySchemes(Arrays.asList(apiKey()));
		//@formatter:on
	}

	private ApiInfo apiInfo() {
		//@formatter:off
		return new ApiInfoBuilder() 
			.title("Roomboo API")
			.description("Roomboo API documentation.")
			.termsOfServiceUrl("")
			.contact(new Contact("Luis Cabrera", "", "lab.cabreraO@gmail.com"))
			.license("GNU General Public License v3.0")
			.licenseUrl("https://raw.githubusercontent.com/labcabrera/roomboo/master/LICENSE")
			.version("0.2.0-SNAPSHOT")
			.build();
		//@formatter:on
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT token", "Authorization", "header");
	}

}
