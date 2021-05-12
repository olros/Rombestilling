package ntnu.idatt2105.user.exception.handler

import ntnu.idatt2105.user.exception.UserNotFoundException
import ntnu.idatt2105.util.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
class UserNotFoundExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    protected fun emailInUseException(ex: UserNotFoundException, request: WebRequest): ResponseEntity<Any> =
            ResponseEntity(Response(ex.message!!) , HttpStatus.NOT_FOUND)
}