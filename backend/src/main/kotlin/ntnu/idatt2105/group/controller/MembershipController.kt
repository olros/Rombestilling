package ntnu.idatt2105.group.controller

import com.querydsl.core.types.Predicate
import io.swagger.annotations.Api
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import ntnu.idatt2105.group.dto.GroupDto
import ntnu.idatt2105.section.dto.SectionDto
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.user.dto.UserIdDto
import ntnu.idatt2105.user.dto.UserListDto
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.util.PaginationConstants
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@Api(value = "Group services", tags = ["Group Services"], description = "Group Services")
@RequestMapping("/groups/{groupId}/memberships/")
interface MembershipController {

    @Operation(summary = "Fetch memberships for the given group", responses = [
        ApiResponse(responseCode = "200", description = "Success"),
        ApiResponse(responseCode = "404", description = "Not found: new group does not exist"),

    ])
    @GetMapping
    fun getMemberships(@QuerydslPredicate(root = User::class) predicate: Predicate,
                       @PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
                       sort= ["firstName"], direction = Sort.Direction.DESC) pageable: Pageable,
                       @PathVariable groupId: UUID): Page<UserListDto>
    @Operation(summary = "Create a new section", responses = [
        ApiResponse(responseCode = "201", description = "Created: new membership was created"),
        ApiResponse(responseCode = "404", description = "Not found: new group or user does not exist"),
    ])
    @PostMapping
    fun createMembership(@QuerydslPredicate(root = User::class) predicate: Predicate,
                         @PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
                         sort= ["firstName"], direction = Sort.Direction.DESC) pageable: Pageable,
                         @PathVariable groupId: UUID,
                         @RequestBody userId: UserIdDto):  Page<UserListDto>
    @DeleteMapping("{userId}/")
    fun deleteMembership(@QuerydslPredicate(root = User::class) predicate: Predicate,
                         @PageableDefault(size = PaginationConstants.PAGINATION_SIZE,
                         sort= ["firstName"], direction = Sort.Direction.DESC) pageable: Pageable,
                         @PathVariable groupId: UUID,
                         @PathVariable userId: UUID):  Page<UserListDto>
}