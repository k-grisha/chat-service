package chat.onmap.chatservice.rest.handler;


import chat.onmap.chatservice.exception.ChatOnMapException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@RestControllerAdvice
public class ChatExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exceptionHandle(Exception e, WebRequest request) throws IOException {
        ContentCachingResponseWrapper cache = new ContentCachingResponseWrapper(
            ((ServletWebRequest) request).getResponse());
        cache.copyBodyToResponse();
        byte[] arr = cache.getContentAsByteArray();
        String json = new String(arr, StandardCharsets.UTF_8);
        System.out.println(json);
        log.error(e.getMessage(), e);
        return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ChatOnMapException.class)
    public ResponseEntity<Object> chatExceptionHandler(ChatOnMapException e) {
        log.warn(e.getMessage(), e);
        return new ResponseEntity<>(e.getMessage(), HttpStatus.valueOf(e.getErrorCode().value));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.warn(e.getMessage(), e);
        String message = e.getBindingResult()
            .getAllErrors()
            .stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.joining(";"));
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

}
