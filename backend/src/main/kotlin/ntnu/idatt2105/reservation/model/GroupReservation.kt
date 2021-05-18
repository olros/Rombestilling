package ntnu.idatt2105.reservation.model

import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.reservation.dto.GroupReservationDto
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.section.dto.toSectionChildrenDto
import ntnu.idatt2105.section.model.Section
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
        nrOfPeople: Int,
        @ManyToOne
        @JoinColumn(name = "section_id", referencedColumnName = "id")
        override var section: Section? = null
): Reservation<Group>() {
        override fun setRelation(entity: Group) {
                this.group = entity
        }
        override fun getEntityId() = group?.id
        override fun toReservationDto(): ReservationDto  =
                        GroupReservationDto(toTime = this.toTime, fromTime = this.fromTime, text = this.text, nrOfPeople =this.nrOfPeople,
                                section =this.section?.toSectionChildrenDto(),
                                group = this.group
                        )

}