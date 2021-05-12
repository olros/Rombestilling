package ntnu.idatt2105.user.controller

import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.service.UserService
import ntnu.idatt2105.util.Constants
import ntnu.idatt2105.util.PaginationConstants
import ntnu.idatt2105.util.Response
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/users/")
class UserController(val userService: UserService) {

    @PostMapping
    fun createUser(@RequestBody userRegistrationDto: @Valid UserRegistrationDto): ResponseEntity<UserDto>  =
            ResponseEntity(userService.createUser(userRegistrationDto), HttpStatus.CREATED)

    @GetMapping("me/")
    fun getMe(authentication: Authentication?): ResponseEntity<Any> {
        return if(authentication != null && authentication.principal != null){
            val user = authentication.principal as UserDetails
            ResponseEntity(userService.getUserDataByEmail(user.username), HttpStatus.OK)
        } else ResponseEntity(Response("You have to be logged in"), HttpStatus.FORBIDDEN)
    }

    @GetMapping("{userId}/")
    fun getUser(@PathVariable userId: UUID,
                @PageableDefault(size = PaginationConstants.PAGINATION_SIZE, sort="firstName", direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Page<UserDto>> =
        ResponseEntity(userService.getUsers(pageable), HttpStatus.OK)

    @PutMapping("{userId}/")
    fun updateUser(@PathVariable userId: UUID, @RequestBody user: UserDto): ResponseEntity<UserDto> =
            ResponseEntity(userService.updateUser(userId, user), HttpStatus.OK)
}