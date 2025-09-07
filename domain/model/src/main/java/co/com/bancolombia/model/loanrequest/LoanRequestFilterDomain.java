package co.com.bancolombia.model.loanrequest;

import co.com.bancolombia.model.common.PageDomain;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class LoanRequestFilterDomain {
    private Long idUser;
    private Long idLoanType;
    private Long term;
    private PageDomain pageDomain;
}
