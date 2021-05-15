package ntnu.idatt2105.security.service

import ntnu.idatt2105.reservation.repository.ReservationRepository
import ntnu.idatt2105.user.model.RoleType
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.RoleRepository
import ntnu.idatt2105.user.repository.UserRepository
import ntnu.idatt2105.user.service.UserDetailsImpl
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*


@Service
class SecurityService(val userRepository: UserRepository, val roleRepository: RoleRepository, val reservationRepository: ReservationRepository){

    private fun getUser(): User? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        val userDetails = authentication?.principal as UserDetailsImpl
        val id = userDetails.getId()
        return userRepository.findById(id).orElse(null)
    }

    fun reservationPermissions(reservationId: UUID) : Boolean {
        val user = getUser()
        val reservation =  reservationRepository.findById(reservationId).orElse(null)
        val isNull = user == null && reservation == null
        return !isNull && ( user!!.roles.contains(roleRepository.findByName(RoleType.ADMIN)) || reservation.user!! == user )

    }
}