package ntnu.idatt2105.security.dto

class LoginRequest {
    var email: String = ""
        get() {
            return field
        }
    set(value) {
        field = value
    }
    var password: String = ""
        get() {
            return field
        }
        set(value) {
            field = value
        }
}