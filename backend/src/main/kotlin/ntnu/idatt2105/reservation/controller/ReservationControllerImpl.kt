package ntnu.idatt2105.reservation.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.reservation.dto.CreateUserReservationRequest
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.dto.ReservationUserDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.model.UserReservation
import ntnu.idatt2105.reservation.service.ReservationService
import ntnu.idatt2105.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
class ReservationControllerImpl(val userReservationService: ReservationService<User>, val groupReservationService: ReservationService<Group>) : ReservationController {

    override fun getAllReservations(
        @RequestParam group: Boolean,
        predicate: Predicate,
        pageable: Pageable,
        sectionId: UUID
    ): Page<ReservationDto> {
        return if (group) {
            groupReservationService.getAllReservation(sectionId, pageable, predicate)
        } else {
            userReservationService.getAllReservation(sectionId, pageable, predicate)
        }
    }

    override fun getReservation(@RequestParam group: Boolean, sectionId: UUID, reservationId: UUID) : ReservationDto {
        return if (group) {
            groupReservationService.getReservation(sectionId, reservationId)
        } else {
            userReservationService.getReservation(sectionId, reservationId)

        }
    }

    override fun createReservation(@RequestParam group: Boolean, sectionId: UUID, reservation: ReservationCreateDto)  : ReservationDto {
        return if (group){
            groupReservationService.createReservation(sectionId, reservation)

        }else{
            userReservationService.createReservation(sectionId, reservation)
        }
    }

    override fun updateReservation(@RequestParam group: Boolean, sectionId: UUID, reservationId: UUID, reservation: ReservationDto) : ReservationDto{
        return if (group){
            groupReservationService.updateReservation(sectionId, reservationId, reservation)
        }else{
            userReservationService.updateReservation(sectionId, reservationId, reservation)
        }
    }

    override fun deleteReservation(@RequestParam group: Boolean, sectionId: UUID, reservationId: UUID) : ResponseEntity<Response> {
        if(group){
            userReservationService.deleteReservation(sectionId, reservationId)

        }else{
            groupReservationService.deleteReservation(sectionId, reservationId)
        }
        return ResponseEntity.ok(Response("Reservation deleted"))
    }
}