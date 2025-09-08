package co.com.bancolombia.api.exceptions;

import lombok.Getter;

@Getter
public class ValidationException extends RuntimeException {

    private final String code;
    private final String message;
    private final Object data;

    public ValidationException(String code, String message, Object data) {
        super(message);
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
