package co.com.bancolombia.model.responsecode;

import lombok.Getter;

@Getter
public enum ResponseCode {
    LOAN_TYPE_NOT_EXISTS("MS002-REQUEST-ERROR001", "The loan type does not exist"),
    STATUS_NOT_EXISTS("MS002-REQUEST-ERROR002", "The status does not exist"),
    USER_NOT_EXISTS("MS001-AUTH-ERROR003", "The user does not exist"),
    TECHNICAL_ERROR("MS002-REQUEST-ERROR004", "A technical error occurred"),
    DATA_CORRUPTED("MS002-REQUEST-ERROR005", "Data is corrupted"),
    DATA_BASE_FAILED("MS002-REQUEST-ERROR006", "Database operation failed"),
    LOAN_AMOUNT_BELOW_MIN("MS002-REQUEST-ERROR007", "The requested amount cannot be lower than the minimum allowed"),
    LOAN_AMOUNT_ABOVE_MAX("MS002-REQUEST-ERROR008", "The requested amount cannot exceed the maximum allowed"),
    UNAUTHORIZED("MS001-AUTH-ERROR009", "Unauthorized access"),
    FORBIDDEN("MS001-AUTH-ERROR010", "Forbidden access"),
    LOAN_REQUEST_ONLY_FOR_SELF("MS002-REQUEST-ERROR009", "The loan request can only be made for yourself"),
    LOAN_REQUEST_CREATED_SUCCESSFULLY("MS002-REQUEST-SUCCESS001", "Loan request created successfully"),
    LOAN_REQUEST_FILTERED_SUCCESSFULLY("MS002-REQUEST-SUCCESS002", "Loan request filtered successfully");



    private final String code;
    private final String defaultMessage;

    ResponseCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}

