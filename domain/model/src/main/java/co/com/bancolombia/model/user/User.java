package co.com.bancolombia.model.user;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long idUser;
    private String identityDocument;
    private String email;
    private BigDecimal baseSalary;
}
