package org.example.univer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@Configuration
@Import(SpringConfig.class)
public class TestSpringConfig {
    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder().addDefaultScripts().setType(EmbeddedDatabaseType.H2).build();
    }
}