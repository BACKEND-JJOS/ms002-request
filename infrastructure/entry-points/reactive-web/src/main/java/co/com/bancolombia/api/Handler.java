package co.com.bancolombia.api;

import co.com.bancolombia.api.mapper.LoanRequestFilterMapper;
import co.com.bancolombia.api.mapper.LoanRequestMapper;
import co.com.bancolombia.api.request.CreditRequest;
import co.com.bancolombia.api.request.FilterLoanRequest;
import co.com.bancolombia.api.request.PaginatedRequest;
import co.com.bancolombia.api.response.ApiResponse;
import co.com.bancolombia.api.validator.GenericValidator;
import co.com.bancolombia.security.SecurityHelper;
import co.com.bancolombia.usecase.filterpendingloanrquest.FilterPendingLoanRequestUseCase;
import co.com.bancolombia.usecase.saveuser.SaveLoanRequestUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final SaveLoanRequestUseCase saveLoanRequestUseCase;
    private final FilterPendingLoanRequestUseCase filterPendingLoanRequestUseCase;
    private static final String RESPONSE_OK = "Response Ok";


    public Mono<ServerResponse> listenPOSTRegisterLoanRequestUseCase(ServerRequest serverRequest) {
        log.info("MESSAGE_HANDLER_LOG_TRACE : INIT METHOD REGISTER LOAN APPLICATION");
        return serverRequest.bodyToMono(CreditRequest.class)
                .flatMap(GenericValidator::validate)
                .flatMap(creditRequest ->  SecurityHelper.getLoggedUserIdentification()
                        .flatMap(loggedIdentification -> {
                            var loanRequest = LoanRequestMapper.toDomain(creditRequest);
                            return saveLoanRequestUseCase.execute(
                                    loanRequest,
                                    loggedIdentification,
                                    creditRequest.getDocumentNumber()
                            );
                        }))
                .doOnSuccess(loanRequest -> log.info("MESSAGE_HANDLER_LOG_TRACE : Successfully created loan request with id={}",loanRequest.getIdLoanRequest()))
                .flatMap(savedLoan -> buildResponse(savedLoan, HttpStatus.CREATED.value(), RESPONSE_OK))
                .doOnError(err -> log.error("MESSAGE_HANDLER_LOG_TRACE : Error while creating loan request - {}", err.getMessage()));
    }

    public Mono<ServerResponse> listenGETFilterLoanRequestPending(ServerRequest serverRequest) {
        log.info("MESSAGE_HANDLER_LOG_TRACE : INIT METHOD FILTER LOAN REQUEST PENDING");

        FilterLoanRequest filterRequest = FilterLoanRequest.builder()
                .userId(serverRequest.queryParam("idUser").map(Long::parseLong).orElse(null))
                .idLoanType(serverRequest.queryParam("idLoanType").map(Long::parseLong).orElse(null))
                .term(serverRequest.queryParam("term").map(Long::parseLong).orElse(null))
                .paginatedRequest(PaginatedRequest.builder()
                        .page(serverRequest.queryParam("page").map(Integer::parseInt).orElse(0))
                        .size(serverRequest.queryParam("size").map(Integer::parseInt).orElse(10))
                        .build()
                )
                .build();

        return Mono.just(filterRequest)
                .flatMap(GenericValidator::validate)
                .map(LoanRequestFilterMapper::toDomain)
                .flatMap(filterPendingLoanRequestUseCase::execute)
                .doOnSuccess(loanRequest -> log.info("MESSAGE_HANDLER_LOG_TRACE : Successfully filtered apply"))
                .flatMap(pageDomain -> buildResponse(pageDomain, HttpStatus.OK.value(), RESPONSE_OK))
                .doOnError(err -> log.error("MESSAGE_HANDLER_LOG_TRACE : Error while filtered apply - {}", err.getMessage()));
    }

    private <T> Mono<ServerResponse> buildResponse(T data, int status, String message) {
        return ServerResponse.status(status).bodyValue(
                ApiResponse.<T>builder()
                        .data(data)
                        .code(message)
                        .build()
        );
    }
}
