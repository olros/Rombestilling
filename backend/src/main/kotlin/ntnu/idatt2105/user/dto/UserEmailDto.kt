package ntnu.idatt2105.user.dto

import ntnu.idatt2105.user.model.User
import java.util.*

data class UserEmailDto (
        val email :String = ""
        )

fun UserEmailDto.toUserListDto() = UserListDto(
        email = this.email
)
