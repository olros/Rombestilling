package ntnu.idatt2105.reservation.controller

import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.service.ReservationService
import ntnu.idatt2105.section.service.SectionService
import ntnu.idatt2105.util.PaginationConstants
import ntnu.idatt2105.util.Response
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/sections/{sectionId}/reservations/")
class ReservationController(val reservationService: ReservationService) {


    @GetMapping
    fun getAllReservations(@PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
            sort= ["fromTime"], direction = Sort.Direction.DESC) pageable: Pageable,
                            @PathVariable sectionId: UUID) =
            reservationService.getAllReservation(sectionId, pageable)

    @GetMapping("{reservationId}/")
    fun getUserReservations(@PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
            sort= ["name"], direction = Sort.Direction.DESC) pageable: Pageable,
                            @PathVariable sectionId: UUID, @PathVariable reservationId: UUID) =
            reservationService.getUserReservation(reservationId, pageable)

    @PostMapping
    fun createReservation(@PathVariable sectionId: UUID, @RequestBody reservation: ReservationCreateDto) =
        reservationService.createReservation(sectionId, reservation)

    @PutMapping("{userId}/")
    fun updateReservation(@PathVariable sectionId: UUID, @PathVariable userId: UUID, @RequestBody reservation: ReservationDto) =
            reservationService.updateReservation(sectionId, userId, reservation)

    @DeleteMapping("{reservationId}/")
    fun deleteReservation(@PathVariable sectionId: UUID, @PathVariable reservationId: UUID) : ResponseEntity<Response> {
        reservationService.deleteReservation(sectionId, reservationId)
        return ResponseEntity.ok(Response("Reservation deleted"))
    }
}