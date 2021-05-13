package ntnu.idatt2105.reservation.model

import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.user.model.User
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

@Entity
data class Reservation(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        var id: UUID = UUID.randomUUID(),
        @ManyToOne
        @JoinColumn(name="user_id", referencedColumnName = "id")
        var user: User? = null,

        @ManyToOne
        @JoinColumn(name="section_id", referencedColumnName = "id")
        var section: Section? = null,

        var fromTime : ZonedDateTime? = null,
        var toTime : ZonedDateTime? = null,
        var text : String = "",
        var nrOfPeople: Int = -1,


        )

