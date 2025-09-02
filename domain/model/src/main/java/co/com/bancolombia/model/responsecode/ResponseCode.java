package co.com.bancolombia.model.responsecode;

public class ResponseCode {
    private ResponseCode() {
        throw new IllegalStateException("Utility class");
    }
    public static final String LOAN_TYPE_NOT_EXISTS = "MS002-REQUEST-ERROR001"; //The loan type does not exist
    public static final String STATUS_NOT_EXISTS = "MS002-REQUEST-ERROR002";
    public static final String USER_NOT_EXISTS = "MS001-AUTH-ERROR003";
    public static final String TECHNICAL_ERROR = "MS002-REQUEST-ERROR004";
    public static final String DATA_CORRUPTED = "MS002-REQUEST-ERROR005";
    public static final String DATA_BASE_FAILED = "MS002-REQUEST-ERROR006";
    public static final String LOAN_AMOUNT_BELOW_MIN = "MS002-REQUEST-ERROR007"; // The requested amount cannot be lower than the minimum allowed
    public static final String LOAN_AMOUNT_ABOVE_MAX = "MS002-REQUEST-ERROR008";// The requested amount cannot exceed the maximum allowed
}
