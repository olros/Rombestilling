package ntnu.idatt2105.group.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.dto.response.Response
import ntnu.idatt2105.group.model.Group
import ntnu.idatt2105.group.service.GroupService
import ntnu.idatt2105.reservation.dto.ReservationDto
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.service.ReservationService
import ntnu.idatt2105.user.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class GroupControllerImpl(val groupService: GroupService,  val reservationService: ReservationService<Group>) : GroupController {
    override fun getGroup(groupId: UUID): ResponseEntity<Group> =
            ResponseEntity(groupService.getGroup(groupId), HttpStatus.OK)

    override fun getGroupReservations(predicate: Predicate,
                                      pageable: Pageable, groupId: UUID) = reservationService.getGroupReservation(groupId, pageable, predicate)

    override fun createGroup(group: Group): ResponseEntity<Group> =
            ResponseEntity(groupService.createGroup(group), HttpStatus.CREATED)


    override fun updateGroup(groupId: UUID, group: Group): ResponseEntity<Group> =
            ResponseEntity(groupService.updateGroup(groupId, group), HttpStatus.OK)


    override fun deleteGroup(groupId: UUID): ResponseEntity<Response> {
        groupService.deleteGroup(groupId)
        return ResponseEntity(Response("Group was deleted"), HttpStatus.OK)
    }
}