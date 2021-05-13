package ntnu.idatt2105.user.service

import ntnu.idatt2105.sercurity.dto.ForgotPassword
import ntnu.idatt2105.sercurity.dto.ResetPasswordDto
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*


interface UserService {
    fun registerUser(user: UserRegistrationDto): UserDto
    fun updateUser(id: UUID, user: UserDto): UserDto
    fun getUsers(pageable: Pageable): Page<UserDto>
    fun forgotPassword(email: ForgotPassword)
    fun resetPassword(resetDto: ResetPasswordDto, id: UUID)
    fun <T> getUser(id: UUID, mapTo: Class<T>): T
}
