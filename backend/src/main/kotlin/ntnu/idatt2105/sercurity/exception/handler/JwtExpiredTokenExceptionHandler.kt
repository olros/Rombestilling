package ntnu.idatt2105.sercurity.exception.handler

import ntnu.idatt2105.sercurity.exception.JwtExpiredTokenException
import ntnu.idatt2105.dto.response.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest

@ControllerAdvice
class JwtExpiredTokenExceptionHandler {

    @ExceptionHandler(JwtExpiredTokenException::class)
    protected fun jwtExpiredTokenException(ex: JwtExpiredTokenException, request: WebRequest): ResponseEntity<Any> =
            ResponseEntity(Response(ex.message!!) , HttpStatus.UNAUTHORIZED)
}