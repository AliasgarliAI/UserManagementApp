package com.company.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@SecuritySchemes({
        @SecurityScheme(name = "basicAuth", type = SecuritySchemeType.HTTP, scheme = "basic"),
        @SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT")
})
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI().info(
                new Info().title("User Management Portal")
                        .version("1.0")
                        .description("Portal for managing users")
                        .license(new License().name("Alakbar Aliasgarli's API Licence").url("https://aliasgarli.com"))
                        .contact(new Contact()
                                .name("Contact Information: \n")
                                .url("https://github.com/AliasgarliAI")
                                .email("eelesgerli98@gmail.com"))

        );
    }
}
