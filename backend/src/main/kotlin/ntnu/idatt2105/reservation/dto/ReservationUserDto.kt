package ntnu.idatt2105.reservation.dto

import ntnu.idatt2105.reservation.model.GroupReservation
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.model.UserReservation
import ntnu.idatt2105.section.dto.SectionChildrenDto
import ntnu.idatt2105.section.dto.toSectionChildrenDto
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.model.User
import java.time.ZonedDateTime
import java.util.*

data class ReservationUserDto(override val id : UUID? = null,
                              override val fromTime: ZonedDateTime? = null,
                              override val toTime: ZonedDateTime? = null,
                              override val text: String = "",
                              override val nrOfPeople: Int = -1,
                              override val section : SectionChildrenDto? = null,
                              val user: UserDto? = null
):
        ReservationDto(id, fromTime, toTime, text, nrOfPeople, section) {
    override fun getEntityId(): UUID? = user?.id

    override fun toReservation(): Reservation<*> = UserReservation(toTime = toTime, fromTime = fromTime, text = text, nrOfPeople = nrOfPeople)

}
