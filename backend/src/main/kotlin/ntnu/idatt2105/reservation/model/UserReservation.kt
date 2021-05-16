package ntnu.idatt2105.reservation.model

import ntnu.idatt2105.user.model.User
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*


@Entity
data class UserReservation(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        override var id: UUID = UUID.randomUUID(),
        @ManyToOne
        @JoinColumn(name = "user_id", referencedColumnName = "id")
        var user: User? = null,
        override var toTime: ZonedDateTime?,
        override var fromTime: ZonedDateTime?,
        override var text: String,
        override var nrOfPeople: Int

) : Reservation<User>() {
    override fun setRelation(entity: User) {
        this.user = entity
    }

    override fun getEntityId() = id

}