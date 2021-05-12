package ntnu.idatt2105.user.controller

import ntnu.idatt2105.user.dto.UserDto
import ntnu.idatt2105.user.dto.UserRegistrationDto
import ntnu.idatt2105.user.service.UserService
import ntnu.idatt2105.util.Response
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
    fun getUserData(authentication: Authentication?): ResponseEntity<Any> {
        return if(authentication != null && authentication.principal != null){
            val user = authentication.principal as UserDetails
            ResponseEntity(userService.getUserDataByEmail(user.username), HttpStatus.OK)
        } else ResponseEntity(Response("You have to be logged in"), HttpStatus.FORBIDDEN)
    }

    @PutMapping("{userId}/")
    fun updateUser(@PathVariable userId: UUID, @RequestBody user: UserDto): ResponseEntity<UserDto> =
            ResponseEntity(userService.updateUser(userId, user), HttpStatus.OK)
}