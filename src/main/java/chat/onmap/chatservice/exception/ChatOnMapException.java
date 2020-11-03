package chat.onmap.chatservice.exception;

import chat.onmap.chatservice.rest.BaseResponse.ErrorCodes;
import lombok.Getter;

@Getter
public class ChatOnMapException extends RuntimeException {
    private final ErrorCodes errorCode;

    public ChatOnMapException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
