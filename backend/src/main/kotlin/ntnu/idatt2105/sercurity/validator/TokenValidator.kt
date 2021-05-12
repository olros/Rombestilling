package ntnu.idatt2105.sercurity.validator

interface TokenValidator {
    fun validate(jti: String)

}