package com.dealersautocenter.api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("render")
public class DatabaseConfig {

    @Bean
    @Primary
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl != null && databaseUrl.startsWith("postgresql://")) {
            try {
                URI dbUri = new URI(databaseUrl);
                
                // Extract credentials
                String username = dbUri.getUserInfo().split(":")[0];
                String password = dbUri.getUserInfo().split(":")[1];
                
                // Build clean JDBC URL without credentials
                int port = dbUri.getPort() != -1 ? dbUri.getPort() : 5432;
                String cleanJdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ":" + port + dbUri.getPath();
                
                System.out.println("Clean JDBC URL: " + cleanJdbcUrl);
                System.out.println("Username: " + username);
                
                return DataSourceBuilder.create()
                        .driverClassName("org.postgresql.Driver")
                        .url(cleanJdbcUrl)
                        .username(username)
                        .password(password)
                        .build();
                        
            } catch (Exception e) {
                throw new RuntimeException("Invalid DATABASE_URL format: " + databaseUrl, e);
            }
        }
        
        // Fallback to default configuration
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/dealer_management")
                .username("dealer_user")
                .password("password")
                .build();
    }
}
