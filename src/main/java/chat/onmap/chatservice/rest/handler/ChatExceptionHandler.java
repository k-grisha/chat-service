package chat.onmap.chatservice.rest.handler;


import chat.onmap.chatservice.exception.ChatOnMapException;
import chat.onmap.chatservice.rest.BaseResponse;
import chat.onmap.chatservice.rest.BaseResponse.ErrorCodes;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ChatExceptionHandler {

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> exceptionHandle(Exception e) {
        log.error(e.getMessage(), e);
        return BaseResponse.fail(ErrorCodes.INTERNAL_ERROR, e.getMessage());
    }

    @ExceptionHandler(ChatOnMapException.class)
    public BaseResponse<Void> chatExceptionHandler(ChatOnMapException e) {
        log.warn(e.getMessage(), e);
        return BaseResponse.fail(e.getErrorCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Void> validationExceptionHandler(MethodArgumentNotValidException e) {
        log.warn(e.getMessage(), e);
        String message = e.getBindingResult()
            .getAllErrors()
            .stream()
            .map(ObjectError::getDefaultMessage)
            .collect(Collectors.joining(";"));
        return BaseResponse.fail(ErrorCodes.INVALID_VALUE, message);
    }

}
