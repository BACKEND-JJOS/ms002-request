package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.request.CreditRequest;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loantype.LoanType;


public class LoanRequestMapper {

    private LoanRequestMapper() {}

    public static LoanRequest toDomain(CreditRequest creditRequest) {
        return LoanRequest.builder()
                .amount(creditRequest.getAmount())
                .term(creditRequest.getTerm())
                .loanType(LoanType.builder()
                                .idLoanType(creditRequest.getLoanTypeId())
                                .build()
                        )
                .build();
    }
}
