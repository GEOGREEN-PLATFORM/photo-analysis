package com.example.photo_analysis.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

import static com.example.photo_analysis.util.AuthorizationStringUtil.AUTHORIZATION;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API микросервиса photo-analyser",
                description = "Микросервис для анализа фото на наличие борщевика",
                contact = @Contact(
                        name = "Егор Дементьев",
                        email = "dementev.eg@yandex.ru"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"
                )
        ),
        servers = {
                @Server(url = "/", description = "GEOGREEN Server URL")
        }
)
@SecurityScheme(
        name = AUTHORIZATION,
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfiguration {
}