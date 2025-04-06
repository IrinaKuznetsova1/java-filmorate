package ru.yandex.practicum.filmorate.errorHandler;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.yandex.practicum.filmorate.exceptions.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ErrorHandlingControllerAdvice {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Обработка исключения MethodArgumentNotValidException: {}", e.getMessage());
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        log.warn("Обработка исключения ConstraintViolationException: {}", e.getMessage());
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(violation -> new Violation(
                        violation.getPropertyPath().toString(),
                        violation.getMessage()))
                .collect(Collectors.toList());
        return new ValidationErrorResponse(violations);
    }

    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Violation> onNotFoundException(NotFoundException e) {
        log.warn("Обработка исключения NotFoundException: {}", e.getMessage());
        return new ResponseEntity<>(new Violation("id", e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(DuplicatedDataException.class)
    public ResponseEntity<Violation> onDuplicatedDataException(DuplicatedDataException e) {
        log.warn("Обработка исключения DuplicatedDataException: {}", e.getMessage());
        return new ResponseEntity<>(new Violation("E-mail", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Violation> onDateTimeParseException(DateTimeParseException e) {
        log.warn("Обработка исключения DateTimeParseException: {}", e.getMessage());
        return new ResponseEntity<>(new Violation("releaseDate/birthdayDate", "Дата должна быть введена в формате " +
                "'yyyy-MM-dd'"), HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> onHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn("Обработка исключения HttpMessageNotReadableException: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
