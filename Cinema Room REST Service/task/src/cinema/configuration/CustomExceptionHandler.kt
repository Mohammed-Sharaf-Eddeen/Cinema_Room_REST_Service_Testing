package cinema.configuration

import cinema.models.CustomErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler
    fun handleException(exc : CustomException) : ResponseEntity<CustomErrorResponse> {
        val customErrorResponse = CustomErrorResponse(exc.message)

        return ResponseEntity(customErrorResponse, exc.statusCode)
    }

}