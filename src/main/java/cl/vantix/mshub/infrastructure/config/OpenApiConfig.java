package cl.vantix.mshub.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.*;
import io.swagger.v3.oas.annotations.security.*;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "ms-hub-convocatorias API", version = "2.0",
                 description = "Plataforma de Gestión Integral de Convocatorias — Vantix SpA",
                 contact = @Contact(name = "Vantix SpA", email = "contacto@vantix.cl")),
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "JWT")
public class OpenApiConfig {}
