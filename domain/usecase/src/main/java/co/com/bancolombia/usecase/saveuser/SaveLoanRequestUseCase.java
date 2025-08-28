package co.com.bancolombia.usecase.saveuser;

import co.com.bancolombia.model.exceptions.BusinessException;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loanrequest.gateway.LoanRequestRepository;
import co.com.bancolombia.model.loantype.gateway.LoanTypeRepository;
import co.com.bancolombia.model.status.StatusType;
import co.com.bancolombia.model.status.gateway.StatusRepository;
import co.com.bancolombia.model.user.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@RequiredArgsConstructor
public class SaveLoanRequestUseCase {

    private final LoanRequestRepository loanRequestRepository;

    private final LoanTypeRepository loanTypeRepository;

    private final StatusRepository statusRepository;

    private final UserRepository userRepository;

    public Mono<LoanRequest> execute(LoanRequest loanRequest, String identificationUser) {

        return loanTypeRepository.findById(loanRequest.getLoanType().getIdLoanType())
                .switchIfEmpty(Mono.error(new BusinessException("The loan type does not exist")))
                .flatMap(loanType ->
                        validateAmountMinLoan(loanRequest.getAmount(), loanType.getMinAmount())
                                .then(validateAmountMaxLoan(loanRequest.getAmount(), loanType.getMaxAmount()))
                                .then(Mono.just(loanType))
                )

                .flatMap(loanType ->
                        statusRepository.findByName(StatusType.PENDING.getDbName())
                                .switchIfEmpty(Mono.error(new BusinessException(
                                        "Error linking request status, contact admin"
                                )))
                                .map(status -> loanRequest.toBuilder()
                                        .loanType(loanType)
                                        .status(status)
                                        .build())
                )
                .flatMap(loanRequestSet -> userRepository.getByIdentification(identificationUser)
                        .switchIfEmpty(Mono.error(
                                new BusinessException("User not found with identification: " + identificationUser)))
                        .map(user -> loanRequestSet.toBuilder()
                                .user(user)
                                .build())
                )
                .flatMap(loanRequestRepository::save);
    }


    public Mono<Void> validateAmountMinLoan(Double requestedAmount, Double minAmountParametized) {
        return Mono.just(requestedAmount)
                .filter(amount -> amount >= minAmountParametized)
                .switchIfEmpty(Mono.error(new BusinessException(
                        "The requested amount cannot be lower than the minimum allowed."
                )))
                .then();
    }

    public Mono<Void> validateAmountMaxLoan(Double requestedAmount, Double maxAmountParametized) {
        return Mono.just(requestedAmount)
                .filter(amount -> amount <= maxAmountParametized)
                .switchIfEmpty(Mono.error(new BusinessException(
                        "The requested amount cannot exceed the maximum allowed."
                )))
                .then();
    }



}
