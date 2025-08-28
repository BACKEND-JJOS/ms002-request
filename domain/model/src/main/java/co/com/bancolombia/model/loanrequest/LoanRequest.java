package co.com.bancolombia.model.loanrequest;

import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.user.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class LoanRequest {

    private Integer idLoanRequest;
    private User user;
    private Double amount;
    private Integer term;
    private String email;
    private Status status;
    private LoanType loanType;

}
