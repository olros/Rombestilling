package ntnu.idatt2105.reservation.dto

import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.section.dto.SectionChildrenDto
import ntnu.idatt2105.section.dto.SectionListDto
import ntnu.idatt2105.user.dto.UserDto
import java.time.ZonedDateTime
import java.util.*

data class ReservationDto (
        val id : UUID? = null,
        val fromTime: ZonedDateTime? = null,
        val toTime: ZonedDateTime? = null,
        val text: String = "",
        val nrOfPeople: Int = -1,
        val section : SectionChildrenDto? = null,
        val user : UserDto? = null
)