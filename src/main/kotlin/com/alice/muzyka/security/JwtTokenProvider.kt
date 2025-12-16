package com.alice.muzyka.security

import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors
import jakarta.annotation.PostConstruct // Import @PostConstruct

@Component
class JwtTokenProvider {

    @Value("\${jwt.secret}")
    private lateinit var jwtSecret: String

    @Value("\${jwt.expiration}")
    private var jwtExpirationInMs: Long = 0

    private val logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    private lateinit var key: Key

    init {
        // Initialize key after jwtSecret is set (Spring handles @Value injection)
        // This is a placeholder, in a real app, ensure secret is securely handled and sufficiently long
    }

    @PostConstruct // This ensures init() is called after properties are set
    fun init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    fun generateToken(authentication: Authentication): String {
        val userPrincipal = authentication.principal as User
        val roles = userPrincipal.authorities.map { it.authority }

        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .claim("roles", roles)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()
    }

    fun getUsernameFromJWT(token: String): String {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
            .subject
    }

    fun validateToken(authToken: String): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }
        return false
    }

    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body

        val username = claims.subject
        
        @Suppress("UNCHECKED_CAST")
        val rolesList = claims["roles"] as? List<String> ?: listOf()
        val roles = rolesList.map { SimpleGrantedAuthority(it) }.toList()

        val userPrincipal = User(username, "", roles) // Password not needed here
        return UsernamePasswordAuthenticationToken(userPrincipal, "", roles)
    }
}
