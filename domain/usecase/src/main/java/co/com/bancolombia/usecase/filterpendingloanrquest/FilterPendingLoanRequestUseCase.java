package co.com.bancolombia.usecase.filterpendingloanrquest;

import co.com.bancolombia.model.common.PageDomain;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loanrequest.LoanRequestFilterDomain;
import co.com.bancolombia.model.loanrequest.gateway.LoanRequestRepository;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateway.LoanTypeRepository;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.StatusType;
import co.com.bancolombia.model.status.gateway.StatusRepository;
import co.com.bancolombia.model.user.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class FilterPendingLoanRequestUseCase {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    public Mono<PageDomain<LoanRequest>> execute(LoanRequestFilterDomain filter) {
        return loanRequestRepository.filterPending(
                        filter.getIdUser(),
                        filter.getIdLoanType(),
                        filter.getTerm(),
                        filter.getPageDomain().getSize(),
                        filter.getPageDomain().getPage()
                )
                .flatMap(this::enrichPageWithDetails);
    }

    private Mono<PageDomain<LoanRequest>> enrichPageWithDetails(PageDomain<LoanRequest> pageDomain) {
        return Flux.fromIterable(pageDomain.getContent())
                .flatMap(this::enrichLoanRequest)
                .collectList()
                .map(list -> rebuildPage(pageDomain, list));
    }

    private Mono<LoanRequest> enrichLoanRequest(LoanRequest loanRequestFiltered) {
        return Mono.zip(
                        loanTypeRepository.findById(loanRequestFiltered.getLoanType().getIdLoanType()),
                        statusRepository.findByName(StatusType.PENDING.getDbName())
                )
                .map(tuple -> buildLoanRequest(loanRequestFiltered, tuple.getT1(), tuple.getT2()));
    }

    private LoanRequest buildLoanRequest(LoanRequest base,
                                         LoanType loanType,
                                         Status status) {
        return LoanRequest.builder()
                .idLoanRequest(base.getIdLoanRequest())
                .user(base.getUser())
                .loanType(loanType)
                .status(status)
                .amount(base.getAmount())
                .term(base.getTerm())
                .email(base.getEmail())
                .build();
    }

    private PageDomain<LoanRequest> rebuildPage(PageDomain<LoanRequest> original,
                                                List<LoanRequest> content) {
        return PageDomain.<LoanRequest>builder()
                .content(content)
                .page(original.getPage())
                .size(original.getSize())
                .totalElements(original.getTotalElements())
                .totalPages(original.getTotalPages())
                .build();
    }
}

