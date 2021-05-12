package ntnu.idatt2105.sercurity.service

import ntnu.idatt2105.sercurity.dto.JwtTokenResponse


interface JwtService {
    fun refreshToken(header: String): JwtTokenResponse
}