package ntnu.idatt2105.reservation.dto

import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.validation.ReservationAllowedFromDateTime
import ntnu.idatt2105.reservation.validation.ReservationFromDateTimeBeforeToDateTime
import ntnu.idatt2105.reservation.validation.ReservationMaximumDuration
import java.time.ZonedDateTime
import java.util.*
import javax.validation.constraints.Positive

@ReservationMaximumDuration
@ReservationAllowedFromDateTime
@ReservationFromDateTimeBeforeToDateTime
abstract class ReservationCreateDto(
        open val sectionId: UUID? = null,
        open val fromTime: ZonedDateTime? = null,
        open val toTime: ZonedDateTime? = null,
        open val text: String = "",
        @get:Positive(message = "The number of people must be positive")
        open val nrOfPeople: Int = 1,
        open val entityId :UUID? = null
        ) {
        abstract fun toReservation(): Reservation<*>
}
