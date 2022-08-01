package cinema.configuration

import org.springframework.http.HttpStatus

class CustomException(val error: String, val statusCode: HttpStatus): RuntimeException(error) {
}