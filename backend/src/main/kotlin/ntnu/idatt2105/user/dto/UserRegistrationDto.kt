package ntnu.idatt2105.user.dto

import java.time.LocalDate

data class UserRegistrationDto(val firstName:String, val surname:String, val password:String, val email:String, val phoneNumber: String)
