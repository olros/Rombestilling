package ntnu.idatt2105.user.dto


data class UserRegistrationDto(
    val firstName: String,
    val surname: String,
    val password: String,
    val email: String,
    val phoneNumber: String
)
