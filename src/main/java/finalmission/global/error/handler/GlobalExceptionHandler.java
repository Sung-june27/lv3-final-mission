package finalmission.global.error.handler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import finalmission.global.error.dto.ErrorResponse;
import finalmission.global.error.exception.BadRequestException;
import finalmission.global.error.exception.ForbiddenException;
import finalmission.global.error.exception.NotFoundException;
import finalmission.global.error.exception.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(BAD_REQUEST).body(response);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(NOT_FOUND).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(ForbiddenException e) {
        ErrorResponse response = ErrorResponse.from(e.getMessage());

        return ResponseEntity.status(FORBIDDEN).body(response);
    }
}
