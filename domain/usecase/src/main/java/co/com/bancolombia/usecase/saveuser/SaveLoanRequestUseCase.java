package co.com.bancolombia.usecase.saveuser;

import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loanrequest.gateway.LoanRequestRepository;
import co.com.bancolombia.model.loantype.gateway.LoanTypeRepository;
import co.com.bancolombia.model.responsecode.ResponseCode;
import co.com.bancolombia.model.status.StatusType;
import co.com.bancolombia.model.status.gateway.StatusRepository;
import co.com.bancolombia.model.user.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;


@RequiredArgsConstructor
public class SaveLoanRequestUseCase {

    private final LoanRequestRepository loanRequestRepository;

    private final LoanTypeRepository loanTypeRepository;

    private final StatusRepository statusRepository;

    private final UserRepository userRepository;

    public Mono<LoanRequest> execute(LoanRequest loanRequest,
                                     String loggedIdentification,
                                     String requestIdentification) {

        return Mono.just(loanRequest)
                .filter(lr -> loggedIdentification.equals(requestIdentification))
                .switchIfEmpty(Mono.error(new BusinessException(ResponseCode.LOAN_REQUEST_ONLY_FOR_SELF)))

                .flatMap(lr -> loanTypeRepository.findById(lr.getLoanType().getIdLoanType())
                        .switchIfEmpty(Mono.error(new BusinessException(ResponseCode.LOAN_TYPE_NOT_EXISTS)))

                        .flatMap(loanType -> validateAmountMinLoan(lr.getAmount(), loanType.getMinAmount())
                                .then(validateAmountMaxLoan(lr.getAmount(), loanType.getMaxAmount()))
                                .thenReturn(lr.toBuilder()
                                        .loanType(loanType)
                                        .build())
                        )
                )

                .flatMap(lr -> statusRepository.findByName(StatusType.PENDING.getDbName())
                        .switchIfEmpty(Mono.error(new BusinessException(ResponseCode.STATUS_NOT_EXISTS)))
                        .map(status -> lr.toBuilder()
                                .status(status)
                                .build()
                        )
                )

                .flatMap(lr -> userRepository.getByIdentification(requestIdentification)
                        .switchIfEmpty(Mono.error(new BusinessException(ResponseCode.USER_NOT_EXISTS)))
                        .map(user -> lr.toBuilder()
                                .user(user)
                                .build()
                        )
                )

                .flatMap(loanRequestRepository::save);
    }

    private Mono<Void> validateAmountMinLoan(BigDecimal requestedAmount, BigDecimal minAmountParameterized) {
        return Mono.just(requestedAmount)
                .filter(amount -> amount.compareTo(minAmountParameterized) >= 0)
                .switchIfEmpty(Mono.error(new BusinessException(ResponseCode.LOAN_AMOUNT_BELOW_MIN)))
                .then();
    }

    private Mono<Void> validateAmountMaxLoan(BigDecimal requestedAmount, BigDecimal maxAmountParameterized) {
        return Mono.just(requestedAmount)
                .filter(amount -> amount.compareTo(maxAmountParameterized) <= 0)
                .switchIfEmpty(Mono.error(new BusinessException(ResponseCode.LOAN_AMOUNT_ABOVE_MAX)))
                .then();
    }
}

