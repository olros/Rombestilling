package ntnu.idatt2105.reservation.model

import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.user.model.User
import java.io.Serializable
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.OneToOne

@Embeddable
data class ReservationId(
        @Column(name = "user_id", columnDefinition = "CHAR(32)")
        val userId: UUID? = null,

        @Column(name = "section_id", columnDefinition = "CHAR(32)")
        val sectionId: UUID? = null
): Serializable


