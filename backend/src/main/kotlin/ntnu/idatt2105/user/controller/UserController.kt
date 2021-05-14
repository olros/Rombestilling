package ntnu.idatt2105.user.controller

import com.querydsl.core.types.Predicate
import ntnu.idatt2105.reservation.model.Reservation
import ntnu.idatt2105.reservation.service.ReservationService
import ntnu.idatt2105.user.dto.DetailedUserDto
import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.service.UserDetailsImpl
import ntnu.idatt2105.user.service.UserService
import ntnu.idatt2105.util.PaginationConstants
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.querydsl.binding.QuerydslPredicate
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/users/")
class UserController(val userService: UserService, val reservationService: ReservationService) {

    @PostMapping
    fun registerUser(@RequestBody @Valid userRegistrationDto: UserRegistrationDto): ResponseEntity<UserDto>  =
            ResponseEntity(userService.registerUser(userRegistrationDto), HttpStatus.CREATED)

    @PostMapping("/batch-users/")
    fun registerUserBatch(@RequestParam("file") file: MultipartFile) =
        ResponseEntity(userService.registerUserBatch(file), HttpStatus.CREATED)

    @GetMapping("me/")
    fun getMe(@AuthenticationPrincipal principal: UserDetailsImpl)=
            ResponseEntity.ok(userService.getUser(principal.getId(), mapTo=DetailedUserDto::class.java))

    @GetMapping("{userId}/")
    fun getUser(@PathVariable userId: UUID) =
        ResponseEntity.ok(userService.getUser(userId, mapTo=UserDto::class.java))

    @GetMapping
    fun getUsers(
            @QuerydslPredicate(root = User::class) predicate: Predicate,
            @PageableDefault(size = PaginationConstants.PAGINATION_SIZE, sort= ["firstName"], direction = Sort.Direction.ASC) pageable: Pageable
    ) =
        ResponseEntity(userService.getUsers(pageable, predicate), HttpStatus.OK)

    @PutMapping("{userId}/")
    fun updateUser(@PathVariable userId: UUID, @Valid @RequestBody user: UserDto) =
            ResponseEntity.ok(userService.updateUser(userId, user))

    @DeleteMapping("{userId}/")
    fun deleteUser(@PathVariable userId: UUID) =
        ResponseEntity.ok(userService.deleteUser(userId))

    @GetMapping("me/reservations/")
    fun getUserReservations(
            @QuerydslPredicate(root = Reservation::class) predicate: Predicate,
            @PageableDefault(size = PaginationConstants.PAGINATION_SIZE, sort= ["fromTime"], direction = Sort.Direction.ASC) pageable: Pageable,
            @AuthenticationPrincipal principal: UserDetailsImpl)=
           reservationService.getUserReservation(principal.getId(), pageable, predicate)
}
