package ntnu.idatt2105.user.dto

import java.time.LocalDate
import java.util.*

data class UserDto(val id: UUID, val firstName:String, val surname:String, val email:String, val birthDate: LocalDate)
