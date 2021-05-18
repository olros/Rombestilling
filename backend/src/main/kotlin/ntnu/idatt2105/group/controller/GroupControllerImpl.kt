package ntnu.idatt2105.group.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.group.dto.GroupDto
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.service.GroupService
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GroupControllerImpl(val groupService: GroupService) : GroupController {

    override fun getAllGroups(predicate: Predicate, pageable: Pageable) = groupService.getAllGroups(pageable, predicate)

    override fun getGroup(groupId: UUID): ResponseEntity<GroupDto> =
            ResponseEntity(groupService.getGroup(groupId), HttpStatus.OK)

    override fun createGroup(group: Group): ResponseEntity<GroupDto> =
            ResponseEntity(groupService.createGroup(group), HttpStatus.CREATED)


    override fun updateGroup(groupId: UUID, group: Group): ResponseEntity<GroupDto> =
            ResponseEntity(groupService.updateGroup(groupId, group), HttpStatus.OK)


    override fun deleteGroup(groupId: UUID): ResponseEntity<Response> {
        groupService.deleteGroup(groupId)
        return ResponseEntity(Response("Group was deleted"), HttpStatus.OK)
    }
}