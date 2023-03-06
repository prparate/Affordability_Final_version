package com.affordability.exception.handler;

import com.affordability.dto.response.MessageResponse;
import com.affordability.enums.EMessageResponseType;
import com.affordability.exception.ApplicationException;
import com.affordability.exception.CustomerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

import java.util.List;

import static com.affordability.enums.EMessageResponseType.ERROR;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerDefault implements IBaseExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        MethodArgumentNotValidException.class,
    })
    public List<MessageResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        log.warn("", ex);

        return ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> new MessageResponse(EMessageResponseType.WARNING, error.getDefaultMessage()))
            .toList();
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        IllegalArgumentException.class,
        NotFoundException.class,
        CustomerNotFoundException.class
    })
    public List<MessageResponse> handleInvalidArguments(IllegalArgumentException ex) {

        log.warn("", ex);

        return getMessageResponses(ex.getMessage(), EMessageResponseType.WARNING);
    }


    @ExceptionHandler({
        ApplicationException.class,
    })
    public ResponseEntity<List<MessageResponse>> handleValidationExceptions(ApplicationException ex) {

        log.error("", ex);

        var body = getMessageResponses(ERROR, ex.getMessage());

        return ResponseEntity.status(ex.getHttpStatus()).body(body);
    }

}

