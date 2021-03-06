package com.convention.config;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configuration
 * 
 * @author KimJungJune <91525531@hanmail.net>
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket productApi(){
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.convention"))
				.paths(PathSelectors.ant("/convention/**"))
                .build()
                .apiInfo(apiInfo());
	}


    private ApiInfo apiInfo() {
    	Contact	contact = new Contact("ConventionSupportService", "http://localhost:8080/", "91525531@hanmail.net");

        return new ApiInfo(
                "Spring Boot REST API",
                "Spring Boot REST API for ConventionSupportService",
                "1.0",
                "Terms of service",
                contact,
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList<>()
                );
    }
}
