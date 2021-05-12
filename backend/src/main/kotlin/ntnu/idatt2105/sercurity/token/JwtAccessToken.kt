package ntnu.idatt2105.sercurity.token

data class JwtAccessToken(private val token: String): JwtToken {

    override fun getToken(): String {
        return token
    }
}