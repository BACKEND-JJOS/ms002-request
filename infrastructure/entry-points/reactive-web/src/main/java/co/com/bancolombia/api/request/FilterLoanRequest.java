package co.com.bancolombia.api.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FilterLoanRequest {

    @Positive(message = "userId must be greater than 0")
    private Long userId;

    @Positive(message = "idLoanType must be greater than 0")
    private Long idLoanType;

    @Positive(message = "term must be greater than 0")
    private Long term;

    @Valid
    private PaginatedRequest paginatedRequest;
}
