package ntnu.idatt2105.reservation.model

import com.querydsl.core.annotations.PropertyType
import com.querydsl.core.annotations.QueryEntity
import com.querydsl.core.annotations.QueryType
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.user.model.User
import org.springframework.format.annotation.DateTimeFormat
import java.time.ZonedDateTime
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
@QueryEntity
data class Reservation(
    @Id
	   @Column(columnDefinition = "CHAR(32)")
	   var id: UUID = UUID.randomUUID(),
    @ManyToOne
	   @JoinColumn(name = "user_id", referencedColumnName = "id")
	   var user: User? = null,

    @ManyToOne
	   @JoinColumn(name = "section_id", referencedColumnName = "id")
	   var section: Section? = null,

    var fromTime: ZonedDateTime? = null,
    var toTime: ZonedDateTime? = null,
    var text: String = "",
    var nrOfPeople: Int = -1,

) {
        @Transient
        @QueryType(PropertyType.DATETIME)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        var fromTimeAfter: ZonedDateTime? = null
        @Transient
        @QueryType(PropertyType.DATETIME)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        var toTimeBefore: ZonedDateTime? = null
}
