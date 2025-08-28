package co.com.bancolombia.model.loantype.gateway;

import co.com.bancolombia.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {

    Mono<LoanType> findById(Integer id);
}
