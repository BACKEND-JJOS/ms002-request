package co.com.bancolombia.api.mapper;

import co.com.bancolombia.api.request.FilterLoanRequest;
import co.com.bancolombia.model.common.PageDomain;
import co.com.bancolombia.model.loanrequest.LoanRequestFilterDomain;

public class LoanRequestFilterMapper {

    private LoanRequestFilterMapper() {}

    public static LoanRequestFilterDomain toDomain(FilterLoanRequest request) {
        return LoanRequestFilterDomain.builder()
                .idUser(request.getUserId())
                .idLoanType(request.getIdLoanType())
                .term(request.getTerm())
                .pageDomain(PageDomain.builder()
                        .page(request.getPaginatedRequest().getPage())
                        .size(request.getPaginatedRequest().getSize())
                        .build()
                )
                .build();
    }
}
