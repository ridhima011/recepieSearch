package com.recepies.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.tags(new Tag("Read", "Get operations"),
					  new Tag("Search", "Search operation"),
					  new Tag("Create", "Post operation"),
					  new Tag("Update", "Put operation"),
					  new Tag("Delete", "Delete operation"))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.recepies.controller"))
				.paths(PathSelectors.any())
				.build();
	}
}
