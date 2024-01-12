package com.tuprimernegocio.library.database.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("https://s1.tuprimernegocio.org:8080")
            .allowedMethods("*") // Permite todos los métodos
            .allowedHeaders("*") // Permite todas las cabeceras
            .allowCredentials(true)
            .maxAge(3600);
    }
}


/*
 * 
 * 
 * 
package com.tuprimernegocio.library.database.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:3000")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS")
            .allowCredentials(true)
            .maxAge(3600);
    }
}
 */