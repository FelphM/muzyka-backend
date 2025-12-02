package com.alice.muzyka.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns(
                "http://localhost:5173",       // Tu React local
                "https://jovial-caramel-4807a9.netlify.app",  // (Opcional) Tu futuro dominio en Netlify
                "https://muzyka-backend.onrender.com"  // (Opcional) Tu propio dominio de Render
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true) // <--- ¡IMPORTANTE PARA EL LOGIN!
    }
}