package ntnu.idatt2105.reservation.config

import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.service.GroupServiceImpl
import ntnu.idatt2105.reservation.repository.GroupReservationRepository
import ntnu.idatt2105.reservation.repository.UserReservationRepository
import ntnu.idatt2105.reservation.service.ReservationService
import ntnu.idatt2105.reservation.service.ReservationServiceImpl
import ntnu.idatt2105.section.repository.SectionRepository
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.service.UserServiceImpl
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component


@Component
class ReservationServiceConfig (val modelMapper: ModelMapper,  val sectionRepository: SectionRepository,
                                val userReservationRepository: UserReservationRepository,
                                val groupReservationRepository: GroupReservationRepository,
                                val groupServiceImpl: GroupServiceImpl,
                                val userServiceImpl: UserServiceImpl){

    @Bean
    fun userReservationService (): ReservationService<User> {
        val service : ReservationServiceImpl<User> = ReservationServiceImpl(modelMapper, sectionRepository)
        service.reservationRelationService = userServiceImpl
        service.reservationRepository = userReservationRepository
        return service

    }
    @Bean
    fun groupReservationService (): ReservationService<Group> {
        val service : ReservationServiceImpl<Group> = ReservationServiceImpl(modelMapper, sectionRepository)
        service.reservationRelationService = groupServiceImpl
        service.reservationRepository = groupReservationRepository
        return service

    }
}