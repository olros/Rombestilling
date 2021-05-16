package ntnu.idatt2105.reservation.model

import ntnu.idatt2105.group.model.Group
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
class GroupReservation(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        override var id: UUID = UUID.randomUUID(),
        @ManyToOne
        @JoinColumn(name = "group_id", referencedColumnName = "id")
        var group: Group? = null,
        toTime: ZonedDateTime?,
        fromTime: ZonedDateTime?,
        text: String,
        nrOfPeople: Int
): Reservation<Group>() {
        override fun setRelation(entity: Group) {
                this.group = entity
        }
        override fun getEntityId() = id

}