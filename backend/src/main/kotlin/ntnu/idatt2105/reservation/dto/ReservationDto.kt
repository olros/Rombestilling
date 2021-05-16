package ntnu.idatt2105.reservation.dto

import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.section.dto.SectionChildrenDto
import ntnu.idatt2105.section.dto.SectionListDto
import ntnu.idatt2105.user.dto.UserDto
import java.time.ZonedDateTime
import java.util.*

abstract class ReservationDto (
        open val id : UUID? = null,
        open val fromTime: ZonedDateTime? = null,
        open val toTime: ZonedDateTime? = null,
        open val text: String = "",
        open val nrOfPeople: Int = -1, 
        open val section : SectionChildrenDto? = null,
) {
    abstract fun getEntityId(): UUID?
    
    abstract fun toReservation(): Reservation<*>
}
