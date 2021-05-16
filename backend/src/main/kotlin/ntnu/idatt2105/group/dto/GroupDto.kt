package ntnu.idatt2105.group.dto

import ntnu.idatt2105.group.model.Group
import java.util.*

data class GroupDto(
        var id: UUID? = null,
        var name: String = "",
)

fun Group.toGroupDto() = GroupDto(
        id = this.id,
        name = this.name
)
