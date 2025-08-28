package co.com.bancolombia.r2dbc.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.*;

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
    private Integer userId;

    @Column("monto")
    private Double amount;

    @Column("plazo")
    private Integer term;

    @Column("email")
    private String email;

    @Column("estado_id")
    private Integer statusId;

    @Column("tipo_prestamo_id")
    private Integer loanTypeId;
}
