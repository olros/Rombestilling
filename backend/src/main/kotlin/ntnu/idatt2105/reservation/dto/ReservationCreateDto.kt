package ntnu.idatt2105.reservation.dto

import java.time.ZonedDateTime
import java.util.*

data class ReservationCreateDto(
        val userId: UUID? = null,
        val sectionId: UUID? = null,
        val fromTime: ZonedDateTime? = null,
        val toTime: ZonedDateTime? = null,
        val text: String = "",
        val nrOfPeople: Int = -1,
        )
