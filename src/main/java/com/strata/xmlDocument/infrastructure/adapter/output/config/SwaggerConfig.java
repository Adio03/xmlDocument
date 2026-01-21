package com.strata.xmlDocument.infrastructure.adapter.output.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI appInfo() {
        Server server = new Server();
        server.setUrl("http://localhost:9100");

        return new OpenAPI()
                .servers(List.of(server))
                .info(new Info()
                        .title("XML TEST API")
                        .version("2.0")
                        .description("API for testing XML document handling without authentication.")
                );
    }

    @Bean
    public GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("http")
                .pathsToMatch("/**")
                .build();
    }

}
