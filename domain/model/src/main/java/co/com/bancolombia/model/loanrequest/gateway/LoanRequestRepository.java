package co.com.bancolombia.model.loanrequest.gateway;

import co.com.bancolombia.model.common.PageDomain;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanRequestRepository {

    Mono<LoanRequest> save(LoanRequest loanRequest);

    Mono<PageDomain<LoanRequest>> filterPending(Long user, Long loanType, Long term, Integer size, Integer page);
}
