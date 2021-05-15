package ntnu.idatt2105.user.service

import com.opencsv.bean.CsvToBean
import com.opencsv.bean.CsvToBeanBuilder
import com.querydsl.core.types.Predicate
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
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.time.ZonedDateTime
import java.util.*
import kotlin.concurrent.thread


@Service
class UserServiceImpl(
    val userRepository: UserRepository,
    val modelMapper: ModelMapper,
    val passwordEncoder: BCryptPasswordEncoder,
    val mailService: MailService,
    val passwordResetTokenRepository: PasswordResetTokenRepository
) : UserService {
    val logger = LoggerFactory.getLogger("UserServiceImpl")

    override fun registerUser(userDTO: UserRegistrationDto): UserDto {
        logger.info("Trying to register a user")
        if (existsByEmail(userDTO.email)) {
            logger.error("User already exists")
            throw ApplicationException.throwException(EntityType.USER, ExceptionType.DUPLICATE_ENTITY, "2", userDTO.email)
        }
        val user: User = modelMapper.map(userDTO, User::class.java)
        user.id = UUID.randomUUID()
        val savedUser: User = userRepository.saveAndFlush(user)
        forgotPassword(ForgotPassword(savedUser.email))
        logger.info("${user.id} has been created... Trying to send mail")
        return modelMapper.map(savedUser, UserDto::class.java)
    }

    private fun createUserObj(userDto : UserRegistrationDto): User {
        return User(email = userDto.email, expirationDate = userDto.expirationDate, firstName = userDto.firstName,
            surname = userDto.surname, phoneNumber =  userDto.phoneNumber)
    }

    override fun registerUserBatch(file: MultipartFile): Response {
        throwIfFileEmpty(file)
        logger.info("Trying to register multiple users")
        var fileReader : BufferedReader? = null

        try {
            fileReader = BufferedReader(InputStreamReader(file.inputStream))
            val csvToBean = createCSVToBean(fileReader)
            val listOfDTO: List<UserRegistrationDto> = csvToBean.parse()
            val listOfObj = mutableListOf<User>()
            listOfDTO.forEach {
                listOfObj.add(modelMapper.map(it, User::class.java))
            }
            userRepository.saveAll(listOfObj)
            logger.info("The users have been created. Sending emails...")
            listOfObj.forEach{
                forgotPassword(ForgotPassword(it.email))
            }
        } catch (ex: Exception) {
            throw Exception("Something went wrong during parsing users", ex)
        } finally {
            closeFileReader(fileReader)
        }
        return Response("The users have been created")
    }

    private fun createCSVToBean(fileReader: BufferedReader?): CsvToBean<UserRegistrationDto> =
        CsvToBeanBuilder<UserRegistrationDto>(fileReader)
            .withType(UserRegistrationDto::class.java)
            .withIgnoreLeadingWhiteSpace(true)
            .build()

    private fun closeFileReader(fileReader: BufferedReader?) {
        try {
            fileReader!!.close()
        } catch (ex: IOException) {
            throw RuntimeException("Error during csv import")
        }
    }

    private fun throwIfFileEmpty(file: MultipartFile) {
        if (file.isEmpty) {
            logger.error("File is empty!")
            throw RuntimeException("Empty file")
        }
    }

    private fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun getUsers(pageable: Pageable, predicate: Predicate): Page<UserDto> =
        userRepository.findAll(predicate, pageable).map { user -> modelMapper.map(user, UserDto::class.java) }

    override fun <T> getUser(id: UUID, mapTo: Class<T>): T {
        val user = getUserById(id)
        return modelMapper.map(user, mapTo)
    }

    override fun updateUser(id: UUID, user: UserDto): UserDto {
        val updatedUser = getUserById(id)
            .copy(
                firstName = user.firstName,
                surname = user.surname,
                email = user.email,
                phoneNumber = user.phoneNumber,
                image = user.image,
            )
        logger.info("${user.id} has been updated")
        return modelMapper.map(userRepository.save(updatedUser), UserDto::class.java)
    }

    override fun deleteUser(id: UUID): Response {
        val user = getUserById(id)
        userRepository.delete(user)
        logger.info("${user.id} has been deleted!")
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
        logger.info("Sending mail to ${user.id} for resetting password")
        sendEmail(user.email, properties)
    }

    override fun resetPassword(resetDto: ResetPasswordDto, id: UUID) {
        val token = passwordResetTokenRepository.findById(id).orElseThrow { throw ApplicationException.throwException(EntityType.TOKEN,
            ExceptionType.ENTITY_NOT_FOUND, id.toString())
        }
        val user = getUserByEmail(resetDto.email)
        if(!user.equals(token.user) && token.expirationDate.isAfter(ZonedDateTime.now())) {
            logger.error("$token is not valid for ${user.email}")
            throw ApplicationException.throwException(EntityType.TOKEN,
                ExceptionType.NOT_VALID, id.toString())
        }
        if(user.roles.isEmpty()) user.roles.plus(USER)
        user.password = passwordEncoder.encode(resetDto.password)
        userRepository.save(user)
        logger.info("New password for ${user.email} has been created")
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
