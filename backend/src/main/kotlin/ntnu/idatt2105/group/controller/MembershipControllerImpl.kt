package ntnu.idatt2105.group.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.group.service.MembershipService
import ntnu.idatt2105.user.dto.UserIdDto
import ntnu.idatt2105.user.dto.UserListDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class MembershipControllerImpl(val membershipService: MembershipService) : MembershipController{
    override fun getMemberships(predicate: Predicate, pageable: Pageable, groupId: UUID): Page<UserListDto> =
            membershipService.getMemberships(groupId, predicate, pageable)

    override fun createMembership(predicate: Predicate, pageable: Pageable, groupId: UUID, userId: UserIdDto): Page<UserListDto> =
            membershipService.createMemberships(groupId,userId, predicate, pageable)

    override fun deleteMembership(predicate: Predicate, pageable: Pageable, groupId: UUID, userId: UUID): Page<UserListDto> =
            membershipService.deleteMembership(groupId, userId, predicate, pageable)
}