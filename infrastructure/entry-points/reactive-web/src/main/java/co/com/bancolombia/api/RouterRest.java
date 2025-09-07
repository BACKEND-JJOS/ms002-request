package co.com.bancolombia.api;

import co.com.bancolombia.api.openapidoc.OpenApiDoc;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springdoc.webflux.core.fn.SpringdocRouteBuilder.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.queryParam;

@Configuration
public class RouterRest {

    public static final String BASE_PATH_LOAN_REQUEST = "/v1/request";

    @Bean
    public WebProperties.Resources  resources(){return new WebProperties.Resources();}


    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route()
                .POST(BASE_PATH_LOAN_REQUEST,
                        handler::listenPOSTRegisterLoanRequestUseCase,
                        OpenApiDoc::createLoanRequest)
                .GET(BASE_PATH_LOAN_REQUEST+"/filter-by-pending",
                        handler::listenGETFilterLoanRequestPending,
                        OpenApiDoc::createLoanRequest)
                .build();
    }
}
