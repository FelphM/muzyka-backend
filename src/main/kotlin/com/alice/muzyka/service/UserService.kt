package com.alice.muzyka.service

import com.alice.muzyka.entity.User
import com.alice.muzyka.exception.ConflictException
import com.alice.muzyka.exception.NotFoundException
import com.alice.muzyka.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import com.alice.muzyka.dto.UserCreateRequest
import com.alice.muzyka.dto.UserUpdateRequest

import org.slf4j.LoggerFactory
// ... (other imports)

@Service
class UserService(private val userRepository: UserRepository, private val passwordEncoder: PasswordEncoder) {

    private val logger = LoggerFactory.getLogger(UserService::class.java)

    fun login(email: String, passwordAttempt: String): User? {
        val user = userRepository.findByEmail(email)
        
        if (user == null) {
            logger.info("Login attempt for email {} failed: User not found.", email)
            return null
        }

        logger.debug("Login attempt for email: {}", email)
        logger.debug("  Password Attempt (plaintext): {}", passwordAttempt)
        logger.debug("  Stored Password Hash: {}", user.passwordHash)

        val passwordMatches = passwordEncoder.matches(passwordAttempt, user.passwordHash)

        if (passwordMatches) {
            logger.info("Login successful for email: {}", email)
            // Call the public updateLastLogin method
            return updateLastLogin(user.id as Long)
        } else {
            logger.info("Login attempt for email {} failed: Password mismatch.", email)
            return null
        }
    }

    // ... (existing functions)

    fun findByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    fun findByUsername(username: String): User? {
        return userRepository.findByName(username)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    fun getUserById(id: Long): User {
        return userRepository.findById(id).orElseThrow { NotFoundException("User not found with ID: $id") }
    }

    fun createUser(userRequest: UserCreateRequest): User {
        logger.debug("Attempting to create user with email: {}", userRequest.email)
        if (userRepository.findByEmail(userRequest.email) != null) {
            logger.warn("User creation failed: User with email {} already exists", userRequest.email)
            throw ConflictException("User with email ${userRequest.email} already exists")
        }
        val encodedPassword = passwordEncoder.encode(userRequest.password)
        val newUser = User(
            name = userRequest.name,
            email = userRequest.email,
            passwordHash = encodedPassword,
            role = if (userRequest.email == "admin@gmail.com") "admin" else userRequest.role, // Temporarily assign admin role for testing
            status = userRequest.status
        )
        val savedUser = userRepository.save(newUser)
        logger.info("User created successfully with email: {} and ID: {}", savedUser.email, savedUser.id)
        logger.debug("  Stored Password Hash for new user: {}", savedUser.passwordHash)
        return savedUser
    }

    fun updateUser(id: Long, userRequest: UserUpdateRequest): User {
        logger.info("Attempting to update user with ID: {}", id)
        logger.debug("Received UserUpdateRequest: {}", userRequest)
        val existingUser = userRepository.findById(id).orElseThrow { NotFoundException("User not found with ID: $id") }
        
        val userToUpdate = existingUser.copy(
            name = userRequest.name ?: existingUser.name,
            email = userRequest.email ?: existingUser.email,
            role = userRequest.role ?: existingUser.role,
            status = userRequest.status ?: existingUser.status
        )
        val savedUser = userRepository.save(userToUpdate)
        logger.info("User with ID: {} updated successfully.", savedUser.id)
        logger.debug("Updated user details: {}", savedUser)
        return savedUser
    }

    fun deleteUser(id: Long) {
        logger.info("Attempting to delete user with ID: {}", id)
        if (!userRepository.existsById(id)) {
            logger.warn("Deletion failed: User not found with ID: {}", id)
            throw NotFoundException("User not found with ID: $id")
        }
        userRepository.deleteById(id)
        logger.info("User with ID: {} deleted successfully.", id)
    }


    fun updateLastLogin(userId: Long): User {
        val user = userRepository.findById(userId).orElseThrow { NotFoundException("User not found with ID: $userId") }
        user.lastLogin = Instant.now()
        return userRepository.save(user)
    }
}