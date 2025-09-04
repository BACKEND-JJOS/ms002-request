package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.response.UserResponse;
import co.com.bancolombia.consumer.response.commons.ApiResponse;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.responsecode.ResponseCode;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.UserRepository;
import co.com.bancolombia.security.SecurityHelper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserRestConsumer implements UserRepository{
    private final WebClient client;

    protected final String API_V1_USER = "/v1/user/";

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackUser")
    public Mono<User> getByIdentification(String identification) {
        log.info("MESSAGE_ADAPTER_REST_LOG_TRACE : INIT getByIdentification - id={}", identification);
        return client.get()
                .uri(API_V1_USER + identification)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND) {
                        log.info("MESSAGE_ADAPTER_REST_LOG_TRACE : No user found identification={}", identification);
                        return Mono.empty();
                    }
                    log.error("MESSAGE_ADAPTER_REST_LOG_TRACE : Client error {} for identification={}", response.statusCode(), identification);
                    return Mono.error(new TechnicalException(ResponseCode.TECHNICAL_ERROR));
                })
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                    log.error("MESSAGE_ADAPTER_REST_LOG_TRACE : Server error {} for identification={}", response.statusCode(), identification);
                    return Mono.error(new TechnicalException(ResponseCode.TECHNICAL_ERROR));
                })
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponse>>() {})
                .map(apiResponse -> {
                    UserResponse data = apiResponse.getData();
                    log.info("MESSAGE_ADAPTER_REST_LOG_TRACE : User found identification={}", identification);
                    return User.builder()
                            .idUser(data.getIdUser())
                            .identityDocument(data.getIdentityDocument())
                            .email(data.getEmail())
                            .build();
                })
                .doOnError(err -> log.error("MESSAGE_ADAPTER_REST_LOG_TRACE : Error calling user service identification={} - {}", identification, err.getMessage()))
                .doOnSubscribe(sub -> log.debug("MESSAGE_ADAPTER_REST_LOG_TRACE : Calling user service for identification={}", identification));
    }

    public Mono<User> fallbackUser(String identification, Throwable ex) {
        log.warn("MESSAGE_ADAPTER_REST_LOG_TRACE : Fallback triggered for user identification={} - {}", identification, ex.getMessage());
        return Mono.error(new TechnicalException(
                ResponseCode.TECHNICAL_ERROR
        ));
    }



}
