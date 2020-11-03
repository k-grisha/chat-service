package chat.onmap.chatservice.rest;

import java.beans.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BaseResponse<T> {

    private final T body;
    private final int errorCode;
    private final String message;

    public static <T> BaseResponse<T> success(T body) {
        return new BaseResponse<>(body, ErrorCodes.OK.value, null);
    }

    public static <T> BaseResponse<T> fail(ErrorCodes errorCode) {
        return new BaseResponse<>(null, errorCode.value, null);
    }

    public static <T> BaseResponse<T> fail(ErrorCodes errorCode, String message) {
        return new BaseResponse<>(null, errorCode.value, message);
    }

    @Transient
    public Boolean isSuccess() {
        return this.errorCode == ErrorCodes.OK.value;
    }


    public enum ErrorCodes {
        OK(0),
        INVALID_VALUE(400),
        NOT_FOUND(404),
        USER_ALREADY_EXIST(409),
        INTERNAL_ERROR(500);

        private final int value;


        ErrorCodes(int value) {
            this.value = value;
        }
    }
}
