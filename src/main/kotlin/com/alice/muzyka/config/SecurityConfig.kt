package com.alice.muzyka.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider // Import DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import com.alice.muzyka.security.JwtAuthenticationFilter
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.AuthenticationEntryPoint // Import AuthenticationEntryPoint
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.ServletException
import java.io.IOException
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.config.Customizer

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val customUserDetailsService: CustomUserDetailsService, // Inject CustomUserDetailsService
    private val passwordEncoder: PasswordEncoder
) {

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(customUserDetailsService)
        authProvider.setPasswordEncoder(passwordEncoder)
        return authProvider
    }

    @Bean
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager {
        return authConfig.authenticationManager
    }

    @Bean
    fun jwtAuthenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { request: HttpServletRequest?, response: HttpServletResponse, authException: AuthenticationException? ->
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { csrf -> csrf.disable() }
            .cors(Customizer.withDefaults()) // ✅ Apply CORS configuration using default resolver
            .sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { exceptions -> exceptions.authenticationEntryPoint(jwtAuthenticationEntryPoint()) } // Add entry point
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/v1/users/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll() // Allow registration (POST)
                    .requestMatchers("/api/v1/products/**").permitAll() // Example: allow all product access
                    .requestMatchers("/api/v1/categories/**").permitAll() // Example: allow all category access
                    .requestMatchers("/api/v1/orders/**").authenticated() // Authenticated users can access order endpoints
                    // Admin-only endpoints for users
                    .requestMatchers("/api/v1/users/**").hasRole("ADMIN") // All other /api/v1/users/** require ADMIN role
                    .anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider()) // Set authentication provider
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java) // Add JWT filter
            // Add other security configurations like JWT filters if needed later
        return http.build()
    }
}
