package ntnu.idatt2105.reservation.dto

import ntnu.idatt2105.reservation.validation.ReservationAllowedFromDateTime
import ntnu.idatt2105.reservation.validation.ReservationFromDateTimeBeforeToDateTime
import ntnu.idatt2105.reservation.validation.ReservationMaximumDuration
import java.time.ZonedDateTime
import java.util.*

@ReservationMaximumDuration
@ReservationAllowedFromDateTime
@ReservationFromDateTimeBeforeToDateTime
data class ReservationCreateDto(
        val userId: UUID? = null,
        val sectionId: UUID? = null,
        val fromTime: ZonedDateTime? = null,
        val toTime: ZonedDateTime? = null,
        val text: String = "",
        val nrOfPeople: Int = -1,
        )
