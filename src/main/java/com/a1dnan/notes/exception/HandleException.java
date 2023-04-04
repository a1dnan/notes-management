package com.a1dnan.notes.exception;

import com.a1dnan.notes.domain.HttpResponse;
import com.a1dnan.notes.util.DateUtil;
import jakarta.persistence.NoResultException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage());
        //Get Field Errors from validation
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fieldsMessage = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));
        return new ResponseEntity<>(HttpResponse.builder()
                .reason("Invalid fields : "+ fieldsMessage)
                .status((HttpStatus) status)
                .statusCode(status.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build(), status);
    }
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                .reason(ex.getMessage())
                .status((HttpStatus) statusCode)
                .statusCode(statusCode.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build(), statusCode);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<HttpResponse<?>> illegalStateException(IllegalStateException exception){
        return createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(NoteNotFoundException.class)
    public ResponseEntity<HttpResponse<?>> noteNotFoundException(NoteNotFoundException exception){
        return createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse<?>> noResultException(NoResultException exception){
        return createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<HttpResponse<?>> servletException(ServletException exception){
        return createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse<?>> exception(Exception exception){
        return createHttpErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
    }

    //Pattern of exception
    private ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus httpStatus, String reason, Exception exception) {
        log.error(exception.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                .reason(reason)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .timeStamp(LocalDateTime.now().format(DateUtil.dateTimeFormatter()))
                .build(), httpStatus);
    }

}
