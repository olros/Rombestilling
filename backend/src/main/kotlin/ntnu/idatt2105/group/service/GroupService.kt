package ntnu.idatt2105.group.service

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.group.model.Group
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface GroupService {
    fun createGroup(group: Group) : Group
    fun updateGroup(groupId: UUID, group: Group) : Group
    fun deleteGroup(groupId: UUID)
    fun getGroup(groupId: UUID) : Group
    fun getAllGroups(pageable: Pageable, predicate: Predicate) : Page<Group>

}