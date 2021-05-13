package ntnu.idatt2105.user.model

import java.util.UUID
import java.time.LocalDate

data class UserBuilder(
    val id: UUID = UUID.randomUUID(),
    val firstName: String = "",
    val surname: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val expirationDate: LocalDate = LocalDate.now(),
    val password: String = "",
    val roles: Set<Role> = setOf()
) {
    fun build(): User {
        return User(
            id = id,
            firstName = firstName,
            surname = surname,
            email = email,
            phoneNumber = phoneNumber,
            expirationDate = expirationDate,
            password = password,
            roles = roles
        )
    }
}