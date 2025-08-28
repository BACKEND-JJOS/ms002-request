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
@Table("estado")
public class StatusEntity {
    @Id
    @Column("id_estado")
    private Integer idStatus;

    @Column("nombre")
    private String name;

    @Column("descripcion")
    private String description;
}
