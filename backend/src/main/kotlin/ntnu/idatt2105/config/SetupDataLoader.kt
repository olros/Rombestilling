package ntnu.idatt2105.config

import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*


@Component
class SetupDataLoader (val userRepository: UserRepository, val passwordEncoder: BCryptPasswordEncoder): ApplicationListener<ContextRefreshedEvent?> {
    private var alreadySetup = false


    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if(!alreadySetup){

            if(userRepository.findByEmail("admin@test.com") == null)userRepository.save(User(UUID.randomUUID(), "hei", "hei", "admin@test.com", "12345678",LocalDate.EPOCH, passwordEncoder.encode("admin")))
        }
        alreadySetup = true

    }
}