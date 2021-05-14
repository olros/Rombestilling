package ntnu.idatt2105.reservation.dto

import ntnu.idatt2105.section.dto.SectionChildrenDto
import java.time.ZonedDateTime
import java.util.UUID

data class ReservationUserDto(
    val id: UUID? = null,
    val fromTime: ZonedDateTime? = null,
    val toTime: ZonedDateTime? = null,
    val text: String = "",
    val nrOfPeople: Int = -1,
    val section: SectionChildrenDto? = null
)
