package ntnu.idatt2105.reservation.model

import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.dto.ReservationUserDto
import ntnu.idatt2105.section.dto.toSectionChildrenDto
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.user.dto.toUserDto
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
        override var nrOfPeople: Int,
        @ManyToOne
        @JoinColumn(name = "section_id", referencedColumnName = "id")
        override var section: Section? = null

) : Reservation<User>() {
    override fun setRelation(entity: User) {
        this.user = entity
    }

    override fun getEntityId() = user?.id
    override fun toReservationDto(): ReservationDto  =
            ReservationUserDto(id = this.id, toTime = this.toTime, fromTime = this.fromTime, text = this.text, nrOfPeople =this.nrOfPeople, section = this.section?.toSectionChildrenDto(), user = this.user?.toUserDto())

}