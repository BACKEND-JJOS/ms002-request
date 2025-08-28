package co.com.bancolombia.consumer;

import co.com.bancolombia.consumer.response.UserResponse;
import co.com.bancolombia.consumer.response.commons.ApiResponse;
import co.com.bancolombia.model.exceptions.TechnicalException;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
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
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();
                    if (status.is2xxSuccessful()) {
                        return response.bodyToMono(new ParameterizedTypeReference<ApiResponse<UserResponse>>() {})
                                .flatMap(apiResponse -> {

                                        log.info("MESSAGE_ADAPTER_REST_LOG_TRACE : User found identification={}", identification);
                                        return Mono.just(
                                                User.builder()
                                                    .idUser(apiResponse.getData().getIdUser())
                                                    .identityDocument(apiResponse.getData().getIdentityDocument())
                                                    .email(apiResponse.getData().getEmail())
                                                    .build()
                                        );
                                });
                    } else if (status == HttpStatus.NOT_FOUND) {
                        log.info("MESSAGE_ADAPTER_REST_LOG_TRACE : No user found identification={}", identification);
                        return Mono.empty();
                    } else if (status.is5xxServerError()) {
                        log.error("MESSAGE_ADAPTER_REST_LOG_TRACE : User service internal server error for identification={}", identification);
                        return Mono.error(new RuntimeException("User service internal server error"));
                    } else {
                        log.error("MESSAGE_ADAPTER_REST_LOG_TRACE : Unexpected response status {} for identification={}", status, identification);
                        return Mono.error(new RuntimeException("Unexpected response status: " + status));
                    }
                })
                .doOnError(err -> log.error("MESSAGE_ADAPTER_REST_LOG_TRACE : Error calling user service identification={} - {}", identification, err.getMessage()))
                .doOnSubscribe(sub -> log.debug("MESSAGE_ADAPTER_REST_LOG_TRACE : Calling user service for identification={}", identification));
    }

    public Mono<User> fallbackUser(String identification, Throwable ex) {
        log.warn("MESSAGE_ADAPTER_REST_LOG_TRACE : Fallback triggered for user identification={} - {}", identification, ex.getMessage());
        return Mono.error(new TechnicalException(
                "The user service is currently unavailable. Please try again later."
        ));
    }



}
