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

	public static final String API_KEY_NAME = "jwtToken";

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
			.description(new StringBuilder()
				.append("<strong>Roomboo API documentation</strong>\n")
				.append("<p>")
				.append("To use the API you need to make a POST request with basic authentication to the following endpoint to get the JWT token\n")
				.append("<pre>\n")
				.append("curl -X POST -v -u demo:demo http://localhost:8080/auth\n")
				.append("</pre>\n")
				.append("</p>\n")
				.append("Checkout Roomboo project in <a href=\"https://github.com/labcabrera/roomboo\">https://github.com/labcabrera/roomboo</a>.\n")
				.append("</p>\n")
				.toString())
			.termsOfServiceUrl("")
			.contact(new Contact("Luis Cabrera", "", "lab.cabreraO@gmail.com"))
			.license("GNU General Public License v3.0")
			.licenseUrl("https://raw.githubusercontent.com/labcabrera/roomboo/master/LICENSE")
			.version("0.2.0-SNAPSHOT")
			.build();
		//@formatter:on
	}

	private ApiKey apiKey() {
		return new ApiKey(API_KEY_NAME, "Authorization", "header");
	}
}
