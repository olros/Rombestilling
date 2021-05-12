package ntnu.idatt2105.sercurity

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import ntnu.idatt2105.sercurity.config.JWTConfig
import ntnu.idatt2105.sercurity.token.JwtRefreshToken
import ntnu.idatt2105.sercurity.token.RawJwtAccessToken
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil(val jwtConfig: JWTConfig) {

    fun getEmailFromToken(token: String): String? {
        return Jwts.parserBuilder()
                .setSigningKey(jwtConfig.secret).build()
                .parseClaimsJws(token.replace(jwtConfig.prefix, ""))
                .body.subject
    }

    fun isValidToken(token: String): Boolean {
        return try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.secret).build()
                    .parseClaimsJws(token.replace(jwtConfig.prefix, ""))
            true
        } catch (ex: SignatureException) {
            throw BadCredentialsException("Invalid credentials", ex)
        } catch (ex: MalformedJwtException) {
            throw BadCredentialsException("Invalid credentials", ex)
        } catch (ex: UnsupportedJwtException) {
            throw BadCredentialsException("Invalid credentials", ex)
        } catch (ex: IllegalArgumentException) {
            throw BadCredentialsException("Invalid credentials", ex)
        }
    }

    fun parseToken(token: String?): JwtRefreshToken? {
        val rawJwtAccessToken = RawJwtAccessToken(token!!)
        return JwtRefreshToken.of(rawJwtAccessToken, jwtConfig.secret)
    }

    fun getUserIdFromToken(token: String): UUID? {
        val uuid = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.secret).build()
                .parseClaimsJws(token.replace(jwtConfig.prefix, ""))
                .body["uuid"]
        return UUID.fromString(uuid.toString())
    }
}