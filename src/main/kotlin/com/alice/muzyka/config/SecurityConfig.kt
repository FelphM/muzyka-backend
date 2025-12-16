package com.alice.muzyka.config

import com.alice.muzyka.security.JwtAuthenticationFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
// Imports de CORS
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val customUserDetailsService: CustomUserDetailsService,
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
        return AuthenticationEntryPoint { _: HttpServletRequest?, response: HttpServletResponse, _: AuthenticationException? ->
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
        }
    }

    // --- CONFIGURACIÓN CORS ---
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        // 1. Permitir tu Frontend EXACTO (Sin barra al final)
        configuration.allowedOrigins = listOf(
            "http://localhost:5173", 
            "https://jovial-caramel-4807a9.netlify.app" 
        )
        // 2. Permitir todos los métodos HTTP
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
        // 3. Permitir todas las cabeceras (Authorization, Content-Type, etc.)
        configuration.allowedHeaders = listOf("*")
        // 4. Permitir credenciales (Cookies/Auth Headers)
        configuration.allowCredentials = true
        
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            // Conectamos la configuración de arriba explícitamente
            .cors { it.configurationSource(corsConfigurationSource()) }
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .exceptionHandling { it.authenticationEntryPoint(jwtAuthenticationEntryPoint()) }
            .authorizeHttpRequests { auth ->
                auth
                    // --- RUTAS PÚBLICAS (Sin Login) ---
                    .requestMatchers("/api/v1/users/login", "/api/v1/users/register").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                    .requestMatchers("/api/v1/blogposts/**").permitAll() // TEMPORARY: Permit all for debugging
                    
                    // --- RUTAS DE USUARIO AUTENTICADO ---
                    // Permitir a cualquier usuario autenticado cambiar su contraseña o perfil
                    .requestMatchers(HttpMethod.POST, "/api/v1/users/change-password").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/api/v1/users/profile").authenticated()
                    .requestMatchers(HttpMethod.GET, "/api/v1/users/email/{email}").authenticated()
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/users/profile").authenticated()

                    // --- RUTAS PROTEGIDAS (ADMIN) ---
                    // El resto de las rutas de /users/ requieren rol de ADMIN
                    .requestMatchers("/api/v1/orders/**").authenticated()
                    .requestMatchers("/api/v1/users/**").hasRole("ADMIN")
                    // .requestMatchers(HttpMethod.POST, "/api/v1/blogposts").hasRole("ADMIN")
                    // .requestMatchers(HttpMethod.PUT, "/api/v1/blogposts/**").hasRole("ADMIN")
                    // .requestMatchers(HttpMethod.DELETE, "/api/v1/blogposts/**").hasRole("ADMIN")
                    
                    // El resto requiere autenticación
                    .anyRequest().authenticated()
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        
        return http.build()
    }
}
