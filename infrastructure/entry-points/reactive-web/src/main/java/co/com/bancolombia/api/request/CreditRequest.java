package co.com.bancolombia.api.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class CreditRequest {
    @NotBlank(message = "Document number is required")
    private String documentNumber;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "500000", message = "Minimum amount is 500.000")
    @DecimalMax(value = "500000000", message = "Maximum amount is 500.000.000")
    private Double amount;

    @NotNull(message = "Term is required")
    @Min(value = 1, message = "Minimum term is 1 month")
    private Integer term;

    @NotNull(message = "Loan type is required")
    private Integer loanTypeId;
}
