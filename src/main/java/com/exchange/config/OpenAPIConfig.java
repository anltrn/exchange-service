package com.exchange.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Exchange Rest Api", version = "1.0.0", description = "Exchange Documentation"))
public class OpenAPIConfig {
}
