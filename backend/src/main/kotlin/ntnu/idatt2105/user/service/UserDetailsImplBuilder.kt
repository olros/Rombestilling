package ntnu.idatt2105.user.service

import java.util.UUID

data class UserDetailsImplBuilder(
val id: UUID = UUID.randomUUID(),
val email: String = "",
val password: String = ""
) {
fun build(): UserDetailsImpl {
return UserDetailsImpl(
id = id,
email = email,
password = password
)
}
}