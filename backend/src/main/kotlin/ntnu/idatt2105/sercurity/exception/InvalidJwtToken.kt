package ntnu.idatt2105.sercurity.exception

class InvalidJwtToken(error: String?) : RuntimeException(error) {
    constructor(): this("Invalid Jwt token")

}