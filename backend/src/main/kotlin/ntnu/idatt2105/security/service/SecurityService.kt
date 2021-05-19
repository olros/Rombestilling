package ntnu.idatt2105.security.service

import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.group.repository.GroupRepository
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
class SecurityService(val userRepository: UserRepository,
                      val roleRepository: RoleRepository,
                      val groupRepository: GroupRepository,
                      val reservationRepository: ReservationRepository){

    private fun getUser(): User? {
        val authentication: Authentication? = SecurityContextHolder.getContext().authentication
        val userDetails = authentication?.principal as UserDetails
        val email = userDetails.username
        return userRepository.findByEmail(email)
    }

    fun reservationPermissions(reservationId: UUID) : Boolean {
        val user = getUser()
        val reservation =  reservationRepository.findById(reservationId).orElse(null)
        if(user != null && reservation != null){
            return reservation.user?.equals(user) == true || user.roles.contains(roleRepository.findByName(RoleType.ADMIN))
        }
        return false
    }

    fun groupPermissions(groupId: UUID) : Boolean {
        val user = getUser()
        val group =  groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }
        if(user != null && group != null){
            return group.creator == user || user.roles.contains(roleRepository.findByName(RoleType.ADMIN))
        }
        return false
    }
}