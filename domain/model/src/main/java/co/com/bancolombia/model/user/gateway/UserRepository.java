package co.com.bancolombia.model.user.gateway;

import co.com.bancolombia.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> getByIdentification(String identification);

    Mono<User> getById(Long idUser);
}
