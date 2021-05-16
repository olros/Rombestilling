package ntnu.idatt2105.group.model

import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table


@Entity
@Table(name = "group_table")
data class Group(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
)
