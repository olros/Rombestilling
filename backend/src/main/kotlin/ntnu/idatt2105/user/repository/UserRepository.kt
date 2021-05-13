package ntnu.idatt2105.user.repository

import ntnu.idatt2105.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, UUID> {
    fun findByEmail(email:String): User?
    fun existsByEmail(email: String): Boolean
}
