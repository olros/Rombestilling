package ntnu.idatt2105.sercurity.exception.handler

import ntnu.idatt2105.sercurity.exception.RefreshTokenNotFound
import ntnu.idatt2105.util.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest


@ControllerAdvice
class RefreshTokenNotFoundHandler {

    @ExceptionHandler(RefreshTokenNotFound::class)
    protected fun refreshTokenNotFound(ex: RefreshTokenNotFound, request: WebRequest): ResponseEntity<Any> =
            ResponseEntity(Response(ex.message!!) , HttpStatus.NOT_FOUND)
}