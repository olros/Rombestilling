package ntnu.idatt2105.group.service

import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.repository.GroupRepository
import ntnu.idatt2105.reservation.service.ReservationRelationService
import org.springframework.stereotype.Service
import java.util.*


@Service
class GroupServiceImpl(val groupRepository: GroupRepository) : GroupService, ReservationRelationService<Group> {
    override fun createGroup(group: Group): Group {
        group.id = UUID.randomUUID()
        return groupRepository.save(group)
    }

    override fun updateGroup(groupId: UUID, group: Group): Group {
        getByEntityId(groupId).run {
            val updatedGroup = this.copy(
                    name = group.name
            )
            return groupRepository.save(updatedGroup)
        }
    }

    override fun deleteGroup(groupId: UUID) {
        getByEntityId(groupId).run {
            groupRepository.delete(this)
        }
    }

    override fun getGroup(groupId: UUID): Group {
        return getByEntityId(groupId)
    }

    override fun getByEntityId(id: UUID): Group = groupRepository.findById(id).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, id.toString()) }

}