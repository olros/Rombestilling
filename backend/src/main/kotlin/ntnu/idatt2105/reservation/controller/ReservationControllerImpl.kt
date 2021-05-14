package ntnu.idatt2105.reservation.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.service.ReservationService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
class ReservationControllerImpl(val reservationService: ReservationService) : ReservationController {

    override fun getAllReservations(
        predicate: Predicate,
        pageable: Pageable,
        sectionId: UUID
    ): Page<ReservationDto> =
        reservationService.getAllReservation(sectionId, pageable, predicate)

    override fun getReservation(sectionId: UUID, reservationId: UUID) =
            reservationService.getReservation(sectionId, reservationId)

    override fun createReservation(sectionId: UUID, reservation: ReservationCreateDto) =
        reservationService.createReservation(sectionId, reservation)

    override fun updateReservation(sectionId: UUID, userId: UUID, reservation: ReservationDto) =
            reservationService.updateReservation(sectionId, userId, reservation)

    override fun deleteReservation(sectionId: UUID, reservationId: UUID) : ResponseEntity<Response> {
        reservationService.deleteReservation(sectionId, reservationId)
        return ResponseEntity.ok(Response("Reservation deleted"))
    }
}