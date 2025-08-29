package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loanrequest.gateway.LoanRequestRepository;
import co.com.bancolombia.r2dbc.entity.LoanRequestEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.mapper.LoanRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class LoanRequestReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<LoanRequest, LoanRequestEntity, Integer, LoanRequestReactiveRepository>
        implements LoanRequestRepository {

    public LoanRequestReactiveRepositoryAdapter(LoanRequestReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanRequest.class));
    }

    @Override
    @Transactional
    public Mono<LoanRequest> save(LoanRequest loanRequest) {
        log.info("MESSAGE_ADAPTER_R2DBC_LOG_TRACE: INIT save loan request");
        return Mono.just(loanRequest)
                .map(LoanRequestMapper::toEntity)
                .flatMap(repository::save)
                .map(entity -> LoanRequestMapper.toDomain(entity, loanRequest.getStatus(), loanRequest.getLoanType(), loanRequest.getUser()))
                .doOnSuccess(saved -> log.info("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : Loan request saved id={}", saved.getIdLoanRequest()))
                .doOnError(err -> log.error("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : DB error while saving loan request - {}", err.getMessage()))
                .onErrorMap(err -> new TechnicalException("Database error: Please contact the administrator."));
    }
}
