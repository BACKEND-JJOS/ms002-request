package co.com.bancolombia.usecase.filterpendingloanrquest;

import co.com.bancolombia.model.common.PageDomain;
import co.com.bancolombia.model.loanrequest.LoanRequest;
import co.com.bancolombia.model.loanrequest.LoanRequestFilterDomain;
import co.com.bancolombia.model.loanrequest.LoanRequestWithPayment;
import co.com.bancolombia.model.loanrequest.gateway.LoanRequestRepository;
import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateway.LoanTypeRepository;
import co.com.bancolombia.model.status.Status;
import co.com.bancolombia.model.status.StatusType;
import co.com.bancolombia.model.status.gateway.StatusRepository;
import co.com.bancolombia.model.user.User;
import co.com.bancolombia.model.user.gateway.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
public class FilterPendingLoanRequestUseCase {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    public Mono<PageDomain<LoanRequestWithPayment>> execute(LoanRequestFilterDomain filter) {
        return loanRequestRepository.filterPending(
                        filter.getIdUser(),
                        filter.getIdLoanType(),
                        filter.getTerm(),
                        filter.getPageDomain().getSize(),
                        filter.getPageDomain().getPage()
                )
                .flatMap(this::enrichPageWithDetails);
    }

    private Mono<PageDomain<LoanRequestWithPayment>> enrichPageWithDetails(PageDomain<LoanRequest> pageDomain) {
        return Flux.fromIterable(pageDomain.getContent())
                .flatMap(this::enrichLoanRequest)
                .collectList()
                .map(list -> rebuildPage(pageDomain, list));
    }

    private Mono<LoanRequestWithPayment> enrichLoanRequest(LoanRequest loanRequestFiltered) {
        return Mono.zip(
                        loanTypeRepository.findById(loanRequestFiltered.getLoanType().getIdLoanType()),
                        statusRepository.findByName(StatusType.PENDING.getDbName()),
                        userRepository.getById(loanRequestFiltered.getUser().getIdUser())
                )
                .map(tuple -> buildLoanRequest(loanRequestFiltered, tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }

    private LoanRequestWithPayment buildLoanRequest(LoanRequest base,
                                         LoanType loanType,
                                         Status status,
                                         User user) {

        BigDecimal monthlyPayment = calculateMonthlyPayment(
                base.getAmount(),
                loanType.getInterestRate(),
                base.getTerm()
        );

        return LoanRequestWithPayment.builder()
                .loanRequest(
                        LoanRequest.builder()
                                .idLoanRequest(base.getIdLoanRequest())
                                .user(base.getUser())
                                .loanType(loanType)
                                .status(status)
                                .amount(base.getAmount())
                                .term(base.getTerm())
                                .email(base.getEmail())
                                .user(user != null ? user : new User())
                                .build()
                )
                .monthlyPayment(
                        monthlyPayment
                )
                .build();
    }


    private PageDomain<LoanRequestWithPayment> rebuildPage(PageDomain<LoanRequest> original,
                                                           List<LoanRequestWithPayment> content) {
        return PageDomain.<LoanRequestWithPayment>builder()
                .content(content)
                .page(original.getPage())
                .size(original.getSize())
                .totalElements(original.getTotalElements())
                .totalPages(original.getTotalPages())
                .build();
    }

    /**
     * Calcula la cuota mensual de un préstamo usando la fórmula de anualidades (método francés).
     *
     * Fórmula:
     *   cuota = P * i / (1 - (1 + i)^(-n))
     *
     * Donde:
     *   P = monto del préstamo
     *   i = tasa de interés mensual (tasa anual / 12)
     *   n = número de meses (plazo)
     *
     * @param amount monto solicitado (P)
     * @param annualRate tasa de interés anual en porcentaje (ej: 10 para 10%)
     * @param term número de meses del préstamo (n) en tipo long
     * @return cuota mensual a pagar
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal annualRate, int term) {
        if (annualRate == null || annualRate.compareTo(BigDecimal.ZERO) == 0) {
            return amount.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP);
        }

        BigDecimal annualRateDecimal = annualRate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal monthlyRate = annualRateDecimal.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);


        int months = Math.toIntExact(term);

        BigDecimal numerator = amount.multiply(monthlyRate);
        BigDecimal denominator = BigDecimal.ONE.subtract(
                BigDecimal.ONE.add(monthlyRate).pow(-months, java.math.MathContext.DECIMAL128)
        );

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

}

