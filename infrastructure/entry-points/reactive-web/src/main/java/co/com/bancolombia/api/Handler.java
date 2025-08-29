package co.com.bancolombia.api;

import co.com.bancolombia.api.mapper.LoanRequestMapper;
import co.com.bancolombia.api.request.CreditRequest;
import co.com.bancolombia.api.response.ApiResponse;
import co.com.bancolombia.api.validator.GenericValidator;
import co.com.bancolombia.usecase.saveuser.SaveLoanRequestUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private  final SaveLoanRequestUseCase saveLoanRequestUseCase;
    private static final String RESPONSE_OK = "Response Ok";


    public Mono<ServerResponse> listenPOSTRegisterLoanRequestUseCase(ServerRequest serverRequest) {
        log.info("MESSAGE_HANDLER_LOG_TRACE : INIT METHOD REGISTER LOAN APPLICATION");
        return serverRequest.bodyToMono(CreditRequest.class)
                .flatMap(GenericValidator::validate)
                .flatMap(creditRequest -> {
                    var loanRequest = LoanRequestMapper.toDomain(creditRequest);
                    return saveLoanRequestUseCase.execute(loanRequest, creditRequest.getDocumentNumber());
                })
                .doOnSuccess(loanRequest -> log.info("MESSAGE_HANDLER_LOG_TRACE : Successfully created loan request with id={}",loanRequest.getIdLoanRequest()))
                .flatMap(savedLoan -> buildResponse(savedLoan, HttpStatus.CREATED.value(), RESPONSE_OK))
                .doOnError(err -> log.error("MESSAGE_HANDLER_LOG_TRACE : Error while creating loan request - {}", err.getMessage()));
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
