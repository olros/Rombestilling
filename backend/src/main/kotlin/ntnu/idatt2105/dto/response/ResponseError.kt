package ntnu.idatt2105.dto.response

import java.time.LocalDate

data class ResponseError(val message: String?, val errors: Any?, val status: Status, val timestamp: LocalDate) {

    companion object {
        fun notFound(message: String?, exception: Exception) =
            ResponseError(
                message,
                exception.message,
                Status.NOT_FOUND,
                LocalDate.now()
            )

        fun duplicateEntity(message: String?, exception: Exception): ResponseError =
            ResponseError(
                message,
                exception.message,
                Status.DUPLICATE_ENTITY,
                LocalDate.now()
            )
    }

    enum class Status {
        OK,
        BAD_REQUEST,
        UNAUTHORIZED,
        VALIDATION_EXCEPTION,
        EXCEPTION,
        WRONG_CREDENTIALS,
        ACCESS_DENIED,
        NOT_FOUND,
        DUPLICATE_ENTITY
    }
}