package ntnu.idatt2105.user.service

import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl(val userRepository: UserRepository, val modelMapper: ModelMapper, val passwordEncoder: BCryptPasswordEncoder) : UserService {

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
            .orElseThrow { ApplicationException.throwException(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, id.toString()) }
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
        userRepository.findById(id).orElseThrow { throw ApplicationException.throwException(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, id.toString()) }

}