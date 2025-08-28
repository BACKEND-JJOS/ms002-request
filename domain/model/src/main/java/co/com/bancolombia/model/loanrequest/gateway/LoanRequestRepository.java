package co.com.bancolombia.model.loanrequest.gateway;

import co.com.bancolombia.model.loanrequest.LoanRequest;
import reactor.core.publisher.Mono;

public interface LoanRequestRepository {

    Mono<LoanRequest> save(LoanRequest loanRequest);
}
