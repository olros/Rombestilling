package ntnu.idatt2105.user.service

import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.exception.EmailInUseException
import ntnu.idatt2105.user.exception.UserNotFoundException
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
            throw EmailInUseException()

        val userObj: User = modelMapper.map(user, User::class.java)
        userObj.id = UUID.randomUUID()

        return modelMapper.map(userRepository.save(userObj), UserDto::class.java)
    }

    private fun existsByEmail(email: String): Boolean {
        return userRepository.existsByEmail(email)
    }

    override fun getUsers(pageable: Pageable): Page<UserDto> =
        userRepository.findAll(pageable).map { user -> modelMapper.map(user, UserDto::class.java) }

    override fun getUser(id: UUID): UserDto {
        val user = getUserById(id)
        return modelMapper.map(user, UserDto::class.java)
    }

    override fun updateUser(id: UUID, user: UserDto): UserDto {
        val updatedUser = userRepository.findById(id).orElseThrow { UserNotFoundException() }.copy(
            firstName = user.firstName,
            surname = user.surname,
            email = user.email,
            phoneNumber = user.phoneNumber,
            image = user.image,
        )
        return modelMapper.map(userRepository.save(updatedUser), UserDto::class.java)
    }

    private fun getUserById(id: UUID): User =
        userRepository.findById(id).orElseThrow { UserNotFoundException() }

}