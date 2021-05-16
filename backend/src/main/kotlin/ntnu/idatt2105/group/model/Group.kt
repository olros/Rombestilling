package ntnu.idatt2105.group.model

import ntnu.idatt2105.user.model.User
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "group_table")
data class Group(
        @Id
        @Column(columnDefinition = "CHAR(32)")
        var id: UUID = UUID.randomUUID(),
        var name: String = "",
        @ManyToMany
        @JoinTable(name = "group_user",
                joinColumns = [JoinColumn(name = "group_id", referencedColumnName = "id")],
                inverseJoinColumns = [JoinColumn(name = "user_id", referencedColumnName = "id")])
        var members: MutableList<User> = mutableListOf()
)
