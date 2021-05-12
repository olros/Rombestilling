package ntnu.idatt2105.user.service

import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.exception.EmailInUseException
import ntnu.idatt2105.user.exception.UserNotFoundException
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import org.modelmapper.ModelMapper
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl(val userRepository: UserRepository, val modelMapper: ModelMapper, val passwordEncoder: BCryptPasswordEncoder) : UserService {


    private fun emailExist(email: String): Boolean {
        return userRepository.findByEmail(email) != null
    }
    override fun createUser(user: UserRegistrationDto): UserDto {
        if (emailExist(user.email)) {
            throw EmailInUseException()
        }
        val userObj: User = modelMapper.map(user, User::class.java)
        userObj.password = (passwordEncoder.encode(user.password))
        userObj.id = UUID.randomUUID()
        return modelMapper.map(userRepository.save(userObj), UserDto::class.java)
    }

    override fun getUserDataByEmail(email: String): UserDto {
        userRepository.findByEmail(email).run {
            if(this != null){
                return modelMapper.map(this, UserDto::class.java)
            }
            throw UserNotFoundException()
        }
    }

    override fun updateUser(id: UUID, user: UserDto): UserDto {
        val updatedUser = userRepository.findById(id).orElseThrow { UserNotFoundException() }
        updatedUser.firstName = user.firstName
        updatedUser.surname = user.surname
        updatedUser.email = user.email
        updatedUser.phoneNumber = user.phoneNumber
        return modelMapper.map(userRepository.save(updatedUser), UserDto::class.java)
    }
}