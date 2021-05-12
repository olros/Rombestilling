package ntnu.idatt2105.sercurity.service

import ntnu.idatt2105.sercurity.token.JwtRefreshToken
import ntnu.idatt2105.sercurity.token.JwtToken
import ntnu.idatt2105.sercurity.token.RefreshToken


interface RefreshTokenService {
    fun saveRefreshToken(token: JwtToken): RefreshToken

    fun getByJti(jti: String): RefreshToken

    fun invalidateSubsequentTokens(jti: String)

    fun rotateRefreshToken(oldRefreshToken: JwtRefreshToken, newRefreshToken: JwtRefreshToken)
}