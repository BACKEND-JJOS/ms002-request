package co.com.bancolombia.model.loanrequest;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanRequestWithPayment {
    private LoanRequest loanRequest;
    private BigDecimal monthlyPayment;
}
