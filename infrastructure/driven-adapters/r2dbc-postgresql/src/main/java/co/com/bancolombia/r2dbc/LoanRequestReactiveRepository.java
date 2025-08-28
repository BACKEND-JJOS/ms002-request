package co.com.bancolombia.r2dbc;

import co.com.bancolombia.r2dbc.entity.LoanRequestEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LoanRequestReactiveRepository extends ReactiveCrudRepository<LoanRequestEntity, Integer>, ReactiveQueryByExampleExecutor<LoanRequestEntity> {

}
