package co.com.bancolombia.model.exceptions;

import co.com.bancolombia.model.responsecode.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessUnAuthorizedException extends RuntimeException{

    private final String code;
    private final String message;

    public BusinessUnAuthorizedException(ResponseCode responseCode) {
        super(responseCode.getDefaultMessage());
        this.code = responseCode.getCode();
        this.message = responseCode.getDefaultMessage();
    }
}
