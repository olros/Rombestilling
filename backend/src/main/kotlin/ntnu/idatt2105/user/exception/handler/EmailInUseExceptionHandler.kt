package ntnu.idatt2105.user.exception

import ntnu.idatt2105.util.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
class EmailInUseExceptionHandler {

    @ExceptionHandler(EmailInUseException::class)
    protected fun emailInUseException(ex: EmailInUseException, request: WebRequest): ResponseEntity<Any> =
            ResponseEntity(Response(ex.message!!) , HttpStatus.BAD_REQUEST)
}