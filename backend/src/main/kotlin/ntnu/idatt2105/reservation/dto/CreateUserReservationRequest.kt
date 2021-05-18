package ntnu.idatt2105.reservation.dto

import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.model.UserReservation
import ntnu.idatt2105.user.model.User
import java.time.ZonedDateTime
import java.util.*

data class CreateUserReservationRequest(override val sectionId: UUID? = null,
                                        override val fromTime: ZonedDateTime? = null,
                                        override val toTime: ZonedDateTime? = null,
                                        override val text: String = "",
                                        override val nrOfPeople: Int = 1,
                                        override val entityId: UUID? = null
): ReservationCreateDto(sectionId, fromTime, toTime, text, nrOfPeople, entityId){
    override fun toReservation(): Reservation<User> =
        UserReservation(
                id = UUID.randomUUID(),
                toTime = this.toTime,
                fromTime = this.fromTime,
                text = this.text,
                nrOfPeople = this.nrOfPeople
        )

}