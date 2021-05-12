package ntnu.idatt2105.sercurity.validator

import ntnu.idatt2105.sercurity.exception.InvalidJwtToken
import ntnu.idatt2105.sercurity.service.RefreshTokenService
import ntnu.idatt2105.sercurity.token.RefreshToken
import org.springframework.stereotype.Component
@Component
class JwtTokenValidator(val refreshTokenService: RefreshTokenService) : TokenValidator {

    override fun validate(jti: String) {
        val refreshToken: RefreshToken = refreshTokenService.getByJti(jti)
        if (!refreshToken.isValid) throw InvalidJwtToken()
    }
}