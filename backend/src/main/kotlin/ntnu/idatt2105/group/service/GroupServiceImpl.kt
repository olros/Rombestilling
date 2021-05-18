package ntnu.idatt2105.group.service

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.repository.GroupRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class GroupServiceImpl(val groupRepository: GroupRepository) : GroupService {
    override fun createGroup(group: Group): Group {
        group.id = UUID.randomUUID()
        return groupRepository.save(group)
    }

    override fun updateGroup(groupId: UUID, group: Group): Group {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }.run {
            val updatedGroup = this.copy(
                    name = group.name
            )
            return groupRepository.save(updatedGroup)
        }
    }

    override fun deleteGroup(groupId: UUID) {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }.run {
            groupRepository.delete(this)
        }
    }

    override fun getGroup(groupId: UUID): Group {
        return groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }
    }

    override fun getAllGroups(pageable: Pageable, predicate: Predicate): Page<Group> {
        return groupRepository.findAll(predicate, pageable)
    }
}