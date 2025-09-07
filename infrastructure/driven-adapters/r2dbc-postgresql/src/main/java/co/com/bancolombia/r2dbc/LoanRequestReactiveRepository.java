package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.LoanRequestEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LoanRequestReactiveRepository extends ReactiveCrudRepository<LoanRequestEntity, Integer>, ReactiveQueryByExampleExecutor<LoanRequestEntity> {

    @Query("SELECT * FROM solicitud " +
            "WHERE estado_id = :idStatus " +
            "AND ( :idUser IS NULL OR usuario_id = :idUser )    " +
            "AND ( :idLoanType IS NULL OR tipo_prestamo_id = :idLoanType )    " +
            "AND ( :term IS NULL OR plazo = :term )    " +
            "LIMIT :size OFFSET :offset")
    Flux<LoanRequestEntity> filterPendingNative(@Param("idUser") Long idUser,
                                                @Param("idLoanType") Long idLoanType,
                                                @Param("term") Long term,
                                                @Param("idStatus") Long idStatus,
                                                @Param("size") Integer size,
                                                @Param("offset") Integer offset);

    @Query("SELECT COUNT(*) FROM solicitud " +
            "WHERE estado_id = :idStatus " +
            "AND ( :idUser IS NULL OR usuario_id = :idUser ) " +
            "AND ( :idLoanType IS NULL OR tipo_prestamo_id = :idLoanType ) " +
            "AND ( :term IS NULL OR plazo = :term )")
    Mono<Long> countPendingNative(@Param("idUser") Long idUser,
                                  @Param("idLoanType") Long idLoanType,
                                  @Param("term") Long term,
                                  @Param("idStatus") Long idStatus);

}

