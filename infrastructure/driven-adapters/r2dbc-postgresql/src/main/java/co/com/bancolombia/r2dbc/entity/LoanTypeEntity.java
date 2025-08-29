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
@Table("tipo_prestamo")
public class LoanTypeEntity {

    @Id
    @Column("id_tipo_prestamo")
    private Integer idLoanType;

    @Column("nombre")
    private String name;

    @Column("monto_minimo")
    private Double minAmount;

    @Column("monto_maximo")
    private BigDecimal maxAmount;

    @Column("tasa_interes")
    private BigDecimal interestRate;

    @Column("validacion_automatica")
    private Boolean automaticValidation;
}
