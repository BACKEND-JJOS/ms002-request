package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.common.PageDomain;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loanrequest.gateway.LoanRequestRepository;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.StatusType;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.entity.LoanRequestEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import co.com.bancolombia.r2dbc.mapper.LoanRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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

    @Override
    public Mono<PageDomain<LoanRequest>> filterPending(Long userId, Long idLoanType, Long term, Integer size, Integer page) {
        int offset = page * size;
        log.info("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : INIT filterPending loan requests");
        return countPending(userId, idLoanType, term)
                .zipWith(fetchPending(userId, idLoanType, term, size, offset).collectList())
                .map(tuple -> buildPage(tuple.getT1(), tuple.getT2(), size, page));
    }

    private Mono<Long> countPending(Long userId, Long idLoanType, Long term) {
        return repository.countPendingNative(userId, idLoanType, term, StatusType.PENDING.getDbId())
                .doOnError(err -> log.error("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : DB error while counting loan requests - {}", err.getMessage()))
                .onErrorMap(err -> new TechnicalException("Database error while counting loan requests"));
    }

    private Flux<LoanRequest> fetchPending(Long userId, Long idLoanType, Long term, Integer size, int offset) {
        return repository.filterPendingNative(userId, idLoanType, term, StatusType.PENDING.getDbId(), size, offset)
                .map(LoanRequestMapper::toDomain)
                .doOnComplete(() -> log.info("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : Completed filtering loan requests"))
                .doOnError(err -> log.error("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : DB error while filtering loan requests - {}", err.getMessage()))
                .onErrorMap(err -> new TechnicalException("Database error while filtering loan requests"));
    }

    private PageDomain<LoanRequest> buildPage(Long total, List<LoanRequest> data, int size, int page) {
        return PageDomain.<LoanRequest>builder()
                .content(data)
                .totalElements(total)
                .page(page)
                .size(size)
                .totalPages((int) Math.ceil((double) total / size))
                .build();
    }




}
