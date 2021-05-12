package ntnu.idatt2105.user.service

import ntnu.idatt2105.user.exception.UserNotFoundException
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException



class UserDetailsServiceImpl: UserDetailsService {

    @Autowired
    lateinit var  userRepository: UserRepository
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String): UserDetails{
        val user: User = userRepository.findByEmail(email) ?: throw UserNotFoundException()
        return UserDetailsImpl(user.id,user.email, user.password)
    }

}