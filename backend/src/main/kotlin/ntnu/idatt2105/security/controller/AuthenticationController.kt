package ntnu.idatt2105.security.controller

import ntnu.idatt2105.security.config.JWTConfig
import ntnu.idatt2105.security.dto.ForgotPassword
import ntnu.idatt2105.security.dto.JwtTokenResponse
import ntnu.idatt2105.security.dto.ResetPasswordDto
import ntnu.idatt2105.security.service.JwtService
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
    fun forgotPassword(@RequestBody email: ForgotPassword) {
        return userService.forgotPassword(email)
    }

    @PostMapping("/reset-password/{passwordResetTokenId}/")
    fun resetPassword(@PathVariable passwordResetTokenId: UUID, @RequestBody reset: ResetPasswordDto) {
        return userService.resetPassword(reset, passwordResetTokenId)
    }
}
