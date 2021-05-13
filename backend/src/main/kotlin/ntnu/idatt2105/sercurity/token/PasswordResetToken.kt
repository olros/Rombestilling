package ntnu.idatt2105.sercurity.token

import ntnu.idatt2105.user.model.User
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.*

data class PasswordResetToken(
    @Id @Column(columnDefinition = "CHAR(32)")
    var id: UUID = UUID.randomUUID(),
    @OneToOne(targetEntity = User::class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, referencedColumnName = "id")
    private val user: User,
    private val expirationDate: ZonedDateTime = ZonedDateTime.now().plusMinutes(EXPIRATION.toLong())
) {
    companion object {
        //60 minutes
        private const val EXPIRATION = 60
    }
}
