package co.com.bancolombia.model.status.gateway;

import co.com.bancolombia.model.status.Status;
import reactor.core.publisher.Mono;

public interface StatusRepository {

    Mono<Status> findByName(String name);
}
