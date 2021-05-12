package ntnu.idatt2105.sercurity.repository

import ntnu.idatt2105.sercurity.token.RefreshToken
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*


interface RefreshTokenRepository : JpaRepository<RefreshToken, UUID>
