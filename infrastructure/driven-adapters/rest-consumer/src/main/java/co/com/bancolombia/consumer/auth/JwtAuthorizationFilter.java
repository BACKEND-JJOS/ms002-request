package co.com.bancolombia.consumer.auth;

import co.com.bancolombia.security.SecurityHelper;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;

public class JwtAuthorizationFilter {

    private JwtAuthorizationFilter() {
        throw new IllegalStateException("Utility class");
    }

    public static ExchangeFilterFunction withJwtBearer() {
        return (ClientRequest request, ExchangeFunction next) ->
                SecurityHelper.getBearerToken()
                        .map(token -> ClientRequest.from(request)
                                .headers(headers -> headers.setBearerAuth(token))
                                .build()
                        )
                        .flatMap(next::exchange);
    }
}
