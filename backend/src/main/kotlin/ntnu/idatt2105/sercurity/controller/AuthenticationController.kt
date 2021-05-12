package ntnu.idatt2105.sercurity.controller

import ntnu.idatt2105.sercurity.config.JWTConfig
import ntnu.idatt2105.sercurity.dto.JwtTokenResponse
import ntnu.idatt2105.sercurity.service.JwtService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("auth/")
class AuthenticationController(val jwtConfig: JWTConfig, val jwtService: JwtService) {

    @GetMapping("/refresh-token/")
    fun refreshToken(request: HttpServletRequest): JwtTokenResponse? {
        val header = request.getHeader(jwtConfig.header)
        return jwtService.refreshToken(header)
    }
}