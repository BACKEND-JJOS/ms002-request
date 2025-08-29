package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.responsecode.ResponseCode;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.gateway.StatusRepository;
import co.com.bancolombia.r2dbc.entity.StatusEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
public class StatusReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<Status, StatusEntity,Integer, StatusReactiveRepository>
        implements StatusRepository {

    public StatusReactiveRepositoryAdapter(StatusReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Status.class));
    }

    @Override
    public Mono<Status> findByName(String name) {
        log.info("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : INIT findByName - name={}", name);
        return repository.findByName(name)
                .doOnSuccess(status -> {
                        log.info("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : Status filter by name={}", name);
                })
                .onErrorMap(throwable -> {
                    log.error("MESSAGE_ADAPTER_R2DBC_LOG_TRACE : DB error while finding status name={} - {}", name, throwable.getMessage());
                    return new TechnicalException(ResponseCode.DATA_BASE_FAILED);
                });
    }

}
