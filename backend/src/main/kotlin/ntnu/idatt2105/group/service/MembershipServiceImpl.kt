package ntnu.idatt2105.group.service

import com.querydsl.core.types.ExpressionUtils
import com.querydsl.core.types.Predicate
import ntnu.idatt2105.exception.ApplicationException
import ntnu.idatt2105.exception.EntityType
import ntnu.idatt2105.exception.ExceptionType
import ntnu.idatt2105.group.repository.GroupRepository
import ntnu.idatt2105.user.dto.UserIdDto
import ntnu.idatt2105.user.dto.UserListDto
import ntnu.idatt2105.user.dto.toUserListDto
import ntnu.idatt2105.user.model.QUser
import ntnu.idatt2105.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*


@Service
class MembershipServiceImpl(val groupRepository: GroupRepository, val userRepository: UserRepository) : MembershipService {

    private fun getGroup(id:UUID) = groupRepository.findById(id).orElseThrow{throw ApplicationException.throwException(
            EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, id.toString()) }

    private fun getUser(id :UUID) = userRepository.findById(id).orElseThrow {
        throw ApplicationException.throwException(
                EntityType.USER,
                ExceptionType.ENTITY_NOT_FOUND,
                id.toString()
        )
    }

    override fun getMemberships(groupId: UUID, predicate: Predicate, pageable: Pageable): Page<UserListDto> =
        getGroup(groupId).run {
            val user = QUser.user
            val newPredicate = ExpressionUtils.allOf(predicate, user.groups.any().id.eq(this.id))!!
            return userRepository.findAll(newPredicate, pageable).map { it.toUserListDto() }
        }

    override fun createMemberships(groupId: UUID, userId: UserIdDto, predicate: Predicate, pageable: Pageable): Page<UserListDto> {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }
                .run {
                    val member = getUser(userId.userId!!)
                    member.groups.add(this)
                    userRepository.save(member)
                    val user = QUser.user
                    val newPredicate = ExpressionUtils.allOf(predicate, user.groups.any().id.eq(this.id))!!
                    return userRepository.findAll(newPredicate, pageable).map { it.toUserListDto() }
        }
    }

    override fun deleteMembership(groupId: UUID, userId: UUID) {
        groupRepository.findById(groupId).orElseThrow{throw ApplicationException.throwException(
                EntityType.GROUP, ExceptionType.ENTITY_NOT_FOUND, groupId.toString()) }
                .run {
                    val member = getUser(userId)
                    member.groups.remove(this)
                    userRepository.save(member)
                }
    }



}