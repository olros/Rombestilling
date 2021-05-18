package ntnu.idatt2105.reservation.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.dto.response.Response
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
class ReservationControllerImpl(val reservationService: ReservationService<User>) : ReservationController {

    override fun getAllReservations(
        predicate: Predicate,
        pageable: Pageable,
        sectionId: UUID
    ): Page<ReservationDto> =
        reservationService.getAllReservation(sectionId, pageable, predicate)

    override fun getReservation(sectionId: UUID, reservationId: UUID) =
            reservationService.getReservation(sectionId, reservationId)

    override fun createReservation(sectionId: UUID, reservation: CreateUserReservationRequest)  : ReservationDto=
        reservationService.createReservation(sectionId, reservation)

    override fun updateReservation(sectionId: UUID, reservationId: UUID, reservation: ReservationDto) =
            reservationService.updateReservation(sectionId, reservationId, reservation)

    override fun deleteReservation(sectionId: UUID, reservationId: UUID) : ResponseEntity<Response> {
        reservationService.deleteReservation(sectionId, reservationId)
        return ResponseEntity.ok(Response("Reservation deleted"))
    }
}