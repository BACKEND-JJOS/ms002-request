package co.com.bancolombia.consumer.response;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Long idUser;
    private String names;
    private String lastNames;
    private String identityDocument;
    private String email;
    private BigDecimal baseSalary;
}