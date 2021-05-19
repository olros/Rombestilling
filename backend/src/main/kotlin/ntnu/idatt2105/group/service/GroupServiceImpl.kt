package ntnu.idatt2105.group.service

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.group.dto.GroupDto
import ntnu.idatt2105.group.dto.toGroupDto
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.repository.GroupRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class GroupServiceImpl(val groupRepository: GroupRepository) : GroupService {
    override fun createGroup(group: Group): GroupDto {
        group.id = UUID.randomUUID()
        return groupRepository.save(group).toGroupDto()
    }

    override fun updateGroup(groupId: UUID, group: Group): GroupDto {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }.run {
            val updatedGroup = this.copy(
                    name = group.name
            )
            return groupRepository.save(updatedGroup).toGroupDto()
        }
    }

    override fun deleteGroup(groupId: UUID) {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }.run {
            groupRepository.delete(this)
        }
    }

    override fun getGroup(groupId: UUID): GroupDto {
        val group = groupRepository.findById(groupId).orElseThrow {
            throw ApplicationException.throwException(
                    EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString())
        }
        return group.toGroupDto()
    }

    override fun getAllGroups(pageable: Pageable, predicate: Predicate): Page<GroupDto> {
        return groupRepository.findAll(predicate, pageable).map {
            it.toGroupDto()
        }
    }

    override fun getUserGroups(userId: UUID): List<GroupDto> {
        return groupRepository.findAllByMembers_Id(userId).map { it.toGroupDto() }
    }
}