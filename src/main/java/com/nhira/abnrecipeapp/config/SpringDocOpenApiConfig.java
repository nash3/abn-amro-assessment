package com.nhira.abnrecipeapp.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocOpenApiConfig {
    @Bean
    public OpenAPI vimbaiRecipesAPI() {
        return new OpenAPI()
                .info(new Info().title("Recipes API")
                        .description("Vimbainashe's Recipes API")
                        .version("v1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org"))
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("all-endpoints")
                .pathsToMatch("/**")
                .build();
    }
}
