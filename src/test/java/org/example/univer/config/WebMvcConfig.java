package org.example.univer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SpringWebConfig.class)

public class WebMvcConfig {
}
