package ntnu.idatt2105.sercurity.repository

import ntnu.idatt2105.sercurity.token.PasswordResetToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface PasswordResetTokenRepository: JpaRepository<PasswordResetToken, UUID>

