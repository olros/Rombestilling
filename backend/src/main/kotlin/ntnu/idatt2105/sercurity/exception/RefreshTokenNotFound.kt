package ntnu.idatt2105.sercurity.exception

import javax.persistence.EntityNotFoundException

class RefreshTokenNotFound(val error: String): EntityNotFoundException(error) {
    constructor(): this("Refresh token not found")
}