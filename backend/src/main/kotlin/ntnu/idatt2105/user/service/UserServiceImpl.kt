package ntnu.idatt2105.user.service

import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.sercurity.dto.ForgotPassword
import ntnu.idatt2105.sercurity.token.PasswordResetToken
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import ntnu.idatt2105.mailer.HtmlTemplate
import ntnu.idatt2105.mailer.Mail
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val modelMapper: ModelMapper,
    val passwordEncoder: BCryptPasswordEncoder
) : UserService {

    override fun registerUser(user: UserRegistrationDto): UserDto {
        if (existsByEmail(user.email))
            throw ApplicationException.throwException(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, "2", user.email)

        val userObj: User = modelMapper.map(user, User::class.java)
        userObj.id = UUID.randomUUID()
        //TODO: this need to be set when you create a user userObj.roles.plus(USER)

        return modelMapper.map(userRepository.save(userObj), UserDto::class.java)
    }

    private fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email) ?: false
    }

    override fun getUsers(pageable: Pageable): Page<UserDto> =
        userRepository.findAll(pageable).map { user -> modelMapper.map(user, UserDto::class.java) }

    override fun getUser(id: UUID): UserDto {
        val user = getUserById(id)
        return modelMapper.map(user, UserDto::class.java)
    }

    override fun updateUser(id: UUID, user: UserDto): UserDto {
        val updatedUser = userRepository.findById(id)
            .orElseThrow {
                ApplicationException.throwException(
                    EntityType.USER,
                    ExceptionType.ENTITY_NOT_FOUND,
                    id.toString()
                )
            }
            .copy(
                firstName = user.firstName,
                surname = user.surname,
                email = user.email,
                phoneNumber = user.phoneNumber,
                image = user.image,
            )
        return modelMapper.map(userRepository.save(updatedUser), UserDto::class.java)
    }

    private fun getUserById(id: UUID): User =
        userRepository.findById(id).orElseThrow {
            throw ApplicationException.throwException(
                EntityType.USER,
                ExceptionType.ENTITY_NOT_FOUND,
                id.toString()
            )
        }

    private fun getUserByEmail(email: String): User =
        userRepository.findByEmail(email) ?: throw ApplicationException.throwException(
            EntityType.USER,
            ExceptionType.ENTITY_NOT_FOUND,
            email
        )

    override fun forgotPassword(email: ForgotPassword) {
        val user = getUserByEmail(email.email)
        val token = PasswordResetToken(user = user)
        val properties = mapOf(
            1 to user.firstName + " " + user.surname,
            2 to "https://gidd-idatt2106.web.app/auth/reset-password/" + token.id + "/"
        )
        sendEmail(user.email, properties)
    }

    private fun sendEmail(email: String, properties: Map<Int, String>) {
        val mail =
            Mail("mailadresse",
                properties.getValue(1),
                "Reset password",
                HtmlTemplate("reset password", properties)
            )

    }
}
