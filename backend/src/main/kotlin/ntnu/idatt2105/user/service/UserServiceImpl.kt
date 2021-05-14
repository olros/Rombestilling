package ntnu.idatt2105.user.service

import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.security.dto.ForgotPassword
import ntnu.idatt2105.security.token.PasswordResetToken
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import ntnu.idatt2105.mailer.HtmlTemplate
import ntnu.idatt2105.mailer.Mail
import ntnu.idatt2105.mailer.MailService
import ntnu.idatt2105.security.repository.PasswordResetTokenRepository
import ntnu.idatt2105.security.dto.ResetPasswordDto
import ntnu.idatt2105.user.model.RoleType.USER
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.util.*


@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val modelMapper: ModelMapper,
    val passwordEncoder: BCryptPasswordEncoder,
    val mailService: MailService,
    val passwordResetTokenRepository: PasswordResetTokenRepository
) : UserService {

    override fun registerUser(user: UserRegistrationDto): UserDto {
        if (existsByEmail(user.email))
            throw ApplicationException.throwException(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, "2", user.email)

        val userObj: User = modelMapper.map(user, User::class.java)
        userObj.id = UUID.randomUUID()

        return modelMapper.map(userRepository.save(userObj), UserDto::class.java)
    }

    private fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun getUsers(pageable: Pageable): Page<UserDto> =
        userRepository.findAll(pageable).map { user -> modelMapper.map(user, UserDto::class.java) }

    override fun <T> getUser(id: UUID, mapTo: Class<T>): T {
        val user = getUserById(id)
        return modelMapper.map(user, mapTo)
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

    override fun deleteUser(id: UUID): Response {
        val user = getUserById(id)
        userRepository.delete(user)
        return Response("The user has been deleted")
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
        token.id = passwordResetTokenRepository.save(token).id
        val properties = mapOf(
            1 to user.email,
            2 to "https://rombestilling.vercel.app/auth/reset-password/" + token.id + "/"
        )
        sendEmail(user.email, properties)
    }

    override fun resetPassword(resetDto: ResetPasswordDto, id: UUID) {
        val token = passwordResetTokenRepository.findById(id).orElseThrow { throw ApplicationException.throwException(EntityType.TOKEN,
            ExceptionType.ENTITY_NOT_FOUND, id.toString())
        }
        val user = getUserByEmail(resetDto.email)
        if(!user.equals(token.user) && token.expirationDate.isAfter(ZonedDateTime.now())) {
            throw ApplicationException.throwException(EntityType.TOKEN,
                ExceptionType.NOT_VALID, id.toString())
        }
        if(user.roles.isEmpty()) user.roles.plus(USER)
        user.password = passwordEncoder.encode(resetDto.password)
        userRepository.save(user)
    }

    private fun sendEmail(email: String, properties: Map<Int, String>) {
        val mail =
            Mail("rombestilling@mail.com",
                properties.getValue(1),
                "Reset password",
                HtmlTemplate("reset_password", properties)
            )
        mailService.sendMail(mail)
    }
}
