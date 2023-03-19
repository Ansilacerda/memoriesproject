package hellospringboot.memories.controller;

import hellospringboot.memories.exception.MemoryNotCreateException;
import hellospringboot.memories.exception.MemoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class MemoryAPIExceptionHandler {

    @ExceptionHandler(MemoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String memoryNotFoundHandler(MemoryNotFoundException exception){
        return exception.getMessage();
    }

    @ExceptionHandler(MemoryNotCreateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String memoryNotCreateHandler(MemoryNotCreateException exception) {
        return exception.getMessage();
    }

}
