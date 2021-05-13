package ntnu.idatt2105.sercurity.controller

import ntnu.idatt2105.sercurity.config.JWTConfig
import ntnu.idatt2105.sercurity.dto.ForgotPassword
import ntnu.idatt2105.sercurity.dto.JwtTokenResponse
import ntnu.idatt2105.sercurity.dto.ResetPasswordDto
import ntnu.idatt2105.sercurity.service.JwtService
import org.springframework.security.access.prepost.PreAuthorize
import ntnu.idatt2105.user.service.UserServiceImpl
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("auth/")
class AuthenticationController(val jwtConfig: JWTConfig, val jwtService: JwtService, val userService: UserServiceImpl) {

    @GetMapping("/refresh-token/")
    fun refreshToken(request: HttpServletRequest): JwtTokenResponse? {
        val header = request.getHeader(jwtConfig.header)
        return jwtService.refreshToken(header)
    }

    @PostMapping("/forgot-password/")
    fun forgotPassword(email: ForgotPassword) {
        return userService.forgotPassword(email)
    }

    @PostMapping("/reset-password/{passwordResetTokenId}/")
    fun resetPassword(@PathVariable passwordResetTokenId: UUID, reset: ResetPasswordDto) {
        return userService.resetPassword(reset, passwordResetTokenId)
    }
}
