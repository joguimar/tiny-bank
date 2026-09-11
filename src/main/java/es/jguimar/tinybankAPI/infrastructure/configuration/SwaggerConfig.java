package es.jguimar.tinybankAPI.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Class to config Swagger (springdoc-openapi)
 */
@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI apiInfo() {
		return new OpenAPI()
				.info(new Info()
						.title("Tiny Bank REST API")
						.description("Spring Boot REST API for Tiny Bank")
						.version("1.0.0"));
	}

}
