package com.fiap.satellitetracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Metadados exibidos na pagina do Swagger UI. */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI().info(new Info()
                .title("Tem Satelite Passando Por Mim Agora? - API")
                .version("1.0.0")
                .description("API REST da Global Solution FIAP - Industria Espacial. "
                        + "Fornece satelites, passagens, usuarios, login, localizacoes, "
                        + "alertas e leituras de sensores IoT.")
                .contact(new Contact().name("FIAP - Global Solution")));
    }
}
