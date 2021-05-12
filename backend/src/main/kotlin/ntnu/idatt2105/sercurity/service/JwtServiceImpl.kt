package ntnu.idatt2105.sercurity.service

import ntnu.idatt2105.sercurity.JwtUtil
import ntnu.idatt2105.sercurity.config.JWTConfig
import ntnu.idatt2105.sercurity.dto.JwtTokenResponse
import ntnu.idatt2105.sercurity.exception.InvalidJwtToken
import ntnu.idatt2105.sercurity.exception.RefreshTokenNotFound
import ntnu.idatt2105.sercurity.extractor.TokenExtractor
import ntnu.idatt2105.sercurity.token.JwtAccessToken
import ntnu.idatt2105.sercurity.token.JwtRefreshToken
import ntnu.idatt2105.sercurity.token.TokenFactory
import ntnu.idatt2105.sercurity.validator.TokenValidator
import ntnu.idatt2105.user.model.User
import ntnu.idatt2105.user.repository.UserRepository
import ntnu.idatt2105.user.service.UserDetailsImpl
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service


@Service
class JwtServiceImpl(val jwtUtil: JwtUtil, val jwtConfig: JWTConfig, val tokenFactory: TokenFactory, val tokenExtractor: TokenExtractor,
                     val tokenValidator: TokenValidator,
                     val refreshTokenService: RefreshTokenService,
                     val userRepository: UserRepository): JwtService {



    override fun refreshToken(header: String): JwtTokenResponse {
        val currentJwtRefreshToken: JwtRefreshToken =  getCurrentJwtRefreshToken(header)
        doValidateToken(currentJwtRefreshToken)
        val user: User = getUserFromToken(currentJwtRefreshToken)
        val userDetails: UserDetailsImpl = UserDetailsImpl(user.id, user.email, user.password)

        val accessToken: JwtAccessToken = tokenFactory.createAccessToken(userDetails)
        val refreshToken: JwtRefreshToken = tokenFactory.createRefreshToken(userDetails) as JwtRefreshToken
        refreshTokenService.rotateRefreshToken(currentJwtRefreshToken, refreshToken)
        return JwtTokenResponse(accessToken.getToken(),refreshToken.getToken())

    }

    private fun getCurrentJwtRefreshToken(header: String): JwtRefreshToken {
        val token = tokenExtractor.extract(header)
        return jwtUtil.parseToken(token) ?: TODO()
    }

    private fun getUserFromToken(refreshToken: JwtRefreshToken): User {
        val subject: String = refreshToken.getSubject()
        return userRepository.findByEmail(subject) ?: TODO()
    }

    private fun doValidateToken(refreshToken: JwtRefreshToken) {
        try {
            tokenValidator.validate(refreshToken.getJti())
        } catch (ex: InvalidJwtToken) {
            refreshTokenService.invalidateSubsequentTokens(refreshToken.getJti())
            throw BadCredentialsException("Invalid refresh token", ex)
        } catch (ex: RefreshTokenNotFound) {
            refreshTokenService.invalidateSubsequentTokens(refreshToken.getJti())
            throw BadCredentialsException("Invalid refresh token", ex)
        }
    }
}