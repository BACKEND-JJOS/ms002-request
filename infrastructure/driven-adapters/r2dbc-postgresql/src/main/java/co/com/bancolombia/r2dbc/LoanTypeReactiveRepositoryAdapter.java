package co.com.bancolombia.r2dbc;

import co.com.bancolombia.model.loantype.LoanType;
import co.com.bancolombia.model.loantype.gateway.LoanTypeRepository;
import co.com.bancolombia.r2dbc.entity.LoanTypeEntity;
import co.com.bancolombia.r2dbc.helper.ReactiveAdapterOperations;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class LoanTypeReactiveRepositoryAdapter
        extends ReactiveAdapterOperations<LoanType, LoanTypeEntity,Integer, LoanTypeReactiveRepository>
        implements LoanTypeRepository {

    public LoanTypeReactiveRepositoryAdapter(LoanTypeReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, LoanType.class));
    }
}
