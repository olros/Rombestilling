package ntnu.idatt2105.group.service

import ntnu.idatt2105.group.dto.GroupDto
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.group.dto.CreateGroupDto
import ntnu.idatt2105.group.model.Group
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface GroupService {
    fun createGroup(group: CreateGroupDto, creatorId: UUID) : GroupDto
    fun updateGroup(groupId: UUID, group: Group) : GroupDto
    fun deleteGroup(groupId: UUID)
    fun getGroup(groupId: UUID) : GroupDto
    fun getAllGroups(pageable: Pageable, predicate: Predicate) : Page<GroupDto>
    fun getUserGroups(userId:UUID) : List<GroupDto>

}