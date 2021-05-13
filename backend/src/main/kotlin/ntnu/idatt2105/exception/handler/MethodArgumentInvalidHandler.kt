package ntnu.idatt2105.exception.handler

import ntnu.idatt2105.dto.response.ResponseError
import org.springframework.core.annotation.Order
import java.util.HashMap

import org.springframework.web.bind.MethodArgumentNotValidException

import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.function.Consumer

@Order(1)
@RestControllerAdvice
class MethodArgumentInvalidHandler  {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(exception: MethodArgumentNotValidException): ResponseError? {
        val errorMessages: MutableMap<String, String?> = HashMap()
        exception.bindingResult.fieldErrors.forEach(Consumer { error: FieldError ->
            errorMessages[error.field] = error.defaultMessage
        })

        val message = "One or more method arguments are invalid"

        return ResponseError.validationError(message, errorMessages)
    }
}