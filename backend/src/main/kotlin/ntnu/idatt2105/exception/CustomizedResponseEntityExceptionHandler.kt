package ntnu.idatt2105.exception


import ntnu.idatt2105.dto.response.ResponseError
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception


@ControllerAdvice
@RestController
class CustomizedResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(ApplicationException.EntityNotFoundException::class)
    fun handleNotFountExceptions(ex: Exception, request: WebRequest?): ResponseEntity<*> {

        val responseError: ResponseError = ResponseError.notFound(ex.message, ex)
        return ResponseEntity<Any?>(responseError, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(ApplicationException.DuplicateEntityException::class)
    fun handleNotFountExceptions1(ex: Exception, request: WebRequest?): ResponseEntity<*> {

        val responseError: ResponseError = ResponseError.duplicateEntity(ex.message, ex)
        return ResponseEntity<Any?>(responseError, HttpStatus.CONFLICT)
    }
}