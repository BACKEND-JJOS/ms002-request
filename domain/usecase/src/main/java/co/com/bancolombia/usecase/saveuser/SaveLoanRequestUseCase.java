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

    public Mono<LoanRequest> execute(LoanRequest loanRequest, String identificationUser) {

        return loanTypeRepository.findById(loanRequest.getLoanType().getIdLoanType())
                .switchIfEmpty(Mono.error(new BusinessException(ResponseCode.LOAN_TYPE_NOT_EXISTS)))
                .flatMap(loanType ->
                        validateAmountMinLoan(loanRequest.getAmount(), loanType.getMinAmount())
                                .then(validateAmountMaxLoan(loanRequest.getAmount(), loanType.getMaxAmount()))
                                .then(Mono.just(loanType))
                )

                .flatMap(loanType ->
                        statusRepository.findByName(StatusType.PENDING.getDbName())
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        ResponseCode.STATUS_NOT_EXISTS
                                )))
                                .map(status -> loanRequest.toBuilder()
                                        .loanType(loanType)
                                        .status(status)
                                        .build())
                )
                .flatMap(loanRequestSet -> userRepository.getByIdentification(identificationUser)
                        .switchIfEmpty(Mono.error(
                                new BusinessException(ResponseCode.USER_NOT_EXISTS)))
                        .map(user -> loanRequestSet.toBuilder()
                                .user(user)
                                .build())
                )
                .flatMap(loanRequestRepository::save);
    }


    public Mono<Void> validateAmountMinLoan(BigDecimal requestedAmount, BigDecimal minAmountParameterized) {
        return Mono.just(requestedAmount)
                .filter(amount -> amount.compareTo(minAmountParameterized) >= 0)
                .switchIfEmpty(Mono.error(new BusinessException(
                        ResponseCode.LOAN_AMOUNT_BELOW_MIN
                )))
                .then();
    }

    public Mono<Void> validateAmountMaxLoan(BigDecimal requestedAmount, BigDecimal maxAmountParameterized) {
        return Mono.just(requestedAmount)
                .filter(amount -> amount.compareTo(maxAmountParameterized) <= 0)
                .switchIfEmpty(Mono.error(new BusinessException(
                        ResponseCode.LOAN_AMOUNT_ABOVE_MAX
                )))
                .then();
    }



}
