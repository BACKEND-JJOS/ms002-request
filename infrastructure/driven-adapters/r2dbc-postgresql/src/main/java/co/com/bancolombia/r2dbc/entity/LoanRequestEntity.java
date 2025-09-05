package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("solicitud")
public class LoanRequestEntity {

    @Id
    @Column("id_solicitud")
    private Integer idLoanRequest;

    @Column("usuario_id")
    private Long userId;

    @Column("monto")
    private BigDecimal amount;

    @Column("plazo")
    private Integer term;

    @Column("email")
    private String email;

    @Column("estado_id")
    private Integer statusId;

    @Column("tipo_prestamo_id")
    private Integer loanTypeId;
}
