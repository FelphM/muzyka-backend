package com.alice.muzyka.controller.v1

import com.alice.muzyka.entity.User
import com.alice.muzyka.service.UserService
import com.alice.muzyka.dto.UserCreateRequest
import com.alice.muzyka.dto.UserUpdateRequest
import com.alice.muzyka.dto.LoginRequest
import com.alice.muzyka.dto.JwtResponse // Import JwtResponse
import com.alice.muzyka.security.JwtTokenProvider // Import JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails

@RestController
@RequestMapping("/api/v1/users")
class UserCtrlV1(
    private val userService: UserService,
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: JwtTokenProvider // Inject JwtTokenProvider
) {

    @GetMapping
    fun getAllUsers(): List<User> = userService.getAllUsers()

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<User> {
        val user = userService.getUserById(id)
        return ResponseEntity.ok(user)
    }

    @PostMapping
    fun createUser(@RequestBody userRequest: UserCreateRequest): ResponseEntity<User> {
        val createdUser = userService.createUser(userRequest)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userRequest: UserUpdateRequest): ResponseEntity<User> {
        val updatedUser = userService.updateUser(id, userRequest)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/email/{email}")
    fun getUserByEmail(@PathVariable email: String): ResponseEntity<User> {
        val user = userService.findByEmail(email)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<JwtResponse> { // Change return type
        return try {
            val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(loginRequest.email, loginRequest.password)
            )
            SecurityContextHolder.getContext().authentication = authentication

            val userDetails = authentication.principal as UserDetails
            val authenticatedUser = userService.findByEmail(userDetails.username)

            if (authenticatedUser != null) {
                userService.updateLastLogin(authenticatedUser.id as Long)
                val jwt = tokenProvider.generateToken(authentication) // Generate JWT
                return ResponseEntity.ok(JwtResponse(jwt, authenticatedUser)) // Return JwtResponse
            } else {
                // This case should ideally not be reached if authenticationManager.authenticate succeeded
                ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
            }
        } catch (e: AuthenticationException) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }
}