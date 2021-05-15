package ntnu.idatt2105.reservation.controller

import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.reservation.dto.ReservationCreateDto
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.service.ReservationService
import ntnu.idatt2105.util.PaginationConstants
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@Api(value = "Reservation services", tags = ["Reservation Services"], description = "Reservation Services")
@RequestMapping("/sections/{sectionId}/reservations/")
interface ReservationController {

    @Operation(summary = "Fetch reservations", responses = [ApiResponse(responseCode = "200", description = "Success")])
    @GetMapping
    fun getAllReservations(
            @QuerydslPredicate(root = Reservation::class) predicate: Predicate,
            @PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
            sort= ["fromTime"], direction = Sort.Direction.DESC) pageable: Pageable,
                           @PathVariable sectionId: UUID
    ): Page<ReservationDto>

    @Operation(summary = "Fetch reservation details for the given reservation id", responses = [
        ApiResponse(responseCode = "200", description = "Success"),
        ApiResponse(responseCode = "404", description = "Not found: reservation with the given id does not exist")
    ])
    @GetMapping("{reservationId}/")
    fun getReservation(@PathVariable sectionId: UUID, @PathVariable reservationId: UUID): ReservationDto

    @Operation(summary = "Create a new reservation", responses = [
        ApiResponse(responseCode = "201", description = "Created: new reservation was created"),
        ApiResponse(responseCode = "400", description = "Bad request: new reservation was not created"),
    ])
    @PostMapping
    fun createReservation(@PathVariable sectionId: UUID, @RequestBody @Valid reservation: ReservationCreateDto): ReservationDto

    @Operation(summary = "Update existing reservation", responses = [
        ApiResponse(responseCode = "200", description = "Success: reservation was updated"),
        ApiResponse(responseCode = "400", description = "Bad request: existing reservation was not updated"),
        ApiResponse(responseCode = "404", description = "Not found: reservation with the given id does not exist"),
    ])
    @PutMapping("{userId}/")
    fun updateReservation(
        @PathVariable sectionId: UUID,
        @PathVariable userId: UUID,
        @RequestBody reservation: ReservationDto
    ): ReservationDto

    @Operation(summary = "Delete existing reservation", responses = [
        ApiResponse(responseCode = "200", description = "Success: reservation was deleted"),
        ApiResponse(responseCode = "404", description = "Not found: reservation with the given id does not exist"),
    ])
    @DeleteMapping("{reservationId}/")
    fun deleteReservation(@PathVariable sectionId: UUID, @PathVariable reservationId: UUID): ResponseEntity<Response>
}