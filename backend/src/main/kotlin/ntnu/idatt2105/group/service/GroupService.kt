package ntnu.idatt2105.group.service

import ntnu.idatt2105.group.model.Group
import java.util.*

interface GroupService {
    fun createGroup(group: Group) : Group
    fun updateGroup(groupId: UUID, group: Group) : Group
    fun deleteGroup(groupId: UUID)
    fun getGroup(groupId: UUID) : Group

}