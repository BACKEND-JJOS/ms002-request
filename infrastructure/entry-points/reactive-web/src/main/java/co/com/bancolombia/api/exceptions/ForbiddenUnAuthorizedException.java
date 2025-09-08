package co.com.bancolombia.api.exceptions;

import co.com.bancolombia.model.responsecode.ResponseCode;
import lombok.Getter;

@Getter
public class ForbiddenUnAuthorizedException extends RuntimeException{

    private final String code;
    private final String message;

    public ForbiddenUnAuthorizedException(ResponseCode responseCode) {
        super(responseCode.getDefaultMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getDefaultMessage();
    }
}

