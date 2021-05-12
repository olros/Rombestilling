package ntnu.idatt2105.user.exception

import java.lang.RuntimeException

class EmailInUseException (val error: String): RuntimeException(error) {
    constructor(): this("This email is already in use")
}