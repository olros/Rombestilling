package ntnu.idatt2105.user.service

import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import java.util.*


interface UserService {
    fun createUser(user: UserRegistrationDto): UserDto
    fun getUserDataByEmail(email: String): UserDto
    fun updateUser(id: UUID, user: UserDto): UserDto


}