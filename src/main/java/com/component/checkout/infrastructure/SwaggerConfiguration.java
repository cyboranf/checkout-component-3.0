package com.component.checkout.infrastructure;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    // Swagger UI: http://localhost:8080/swagger-ui/index.html
    // API Docs: http://localhost:8080/v3/api-docs

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("CC3 API")
                        .version("1.0")
                        .description("API documentation for the CC3 application")
                );
    }
}