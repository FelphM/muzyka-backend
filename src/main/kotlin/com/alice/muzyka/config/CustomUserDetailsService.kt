package com.alice.muzyka.config

import com.alice.muzyka.service.UserService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.authority.SimpleGrantedAuthority // Import SimpleGrantedAuthority

import org.slf4j.LoggerFactory

@Service
class CustomUserDetailsService(private val userService: UserService) : UserDetailsService {

    private val logger = LoggerFactory.getLogger(CustomUserDetailsService::class.java)

    override fun loadUserByUsername(identifier: String): UserDetails {
        logger.debug("Attempting to load user by email: {}", identifier)

        val user = userService.findByEmail(identifier)

        if (user == null) {
            logger.info("User not found for email: {}", identifier)
            throw UsernameNotFoundException("User not found with email: $identifier")
        }

        val authorities = listOf(SimpleGrantedAuthority("ROLE_" + user.role.uppercase())) // Convert role to GrantedAuthority

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.passwordHash,
            authorities // Pass the roles here
        )
    }
}
