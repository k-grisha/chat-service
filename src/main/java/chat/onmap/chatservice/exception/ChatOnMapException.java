package chat.onmap.chatservice.exception;


import lombok.Getter;

@Getter
public class ChatOnMapException extends RuntimeException {

    private final ErrorCodes errorCode;

    public ChatOnMapException(String message, ErrorCodes errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public enum ErrorCodes {
        OK(0),
        INVALID_VALUE(400),
        NOT_FOUND(404),
        USER_ALREADY_EXIST(409),
        INTERNAL_ERROR(500);

        public final int value;


        ErrorCodes(int value) {
            this.value = value;
        }
    }
}
