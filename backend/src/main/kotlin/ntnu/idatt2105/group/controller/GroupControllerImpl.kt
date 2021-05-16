package ntnu.idatt2105.group.controller

import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.service.GroupService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GroupControllerImpl(val groupService: GroupService) : GroupController {
    override fun getGroup(groupId: UUID): ResponseEntity<Group> =
            ResponseEntity(groupService.getGroup(groupId), HttpStatus.OK)

    override fun createSection(group: Group): ResponseEntity<Group> =
            ResponseEntity(groupService.createGroup(group), HttpStatus.CREATED)


    override fun updateSection(groupId: UUID, group: Group): ResponseEntity<Group> =
            ResponseEntity(groupService.updateGroup(groupId, group), HttpStatus.OK)


    override fun deleteSection(groupId: UUID): ResponseEntity<Response> {
        groupService.deleteGroup(groupId)
        return ResponseEntity(Response("Group was deleted"), HttpStatus.OK)
    }
}