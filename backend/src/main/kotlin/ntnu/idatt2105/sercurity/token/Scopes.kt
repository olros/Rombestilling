package ntnu.idatt2105.sercurity.token

enum class Scopes {
    REFRESH_TOKEN;

    fun scope(): String {
        return "ROLE_$name"
    }
}