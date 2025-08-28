package co.com.bancolombia.consumer.response;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private Integer idUser;
    private String names;
    private String lastNames;
    private LocalDate dateBirth;
    private String identityDocument;
    private String address;
    private String phone;
    private String email;
    private Integer idRole;
    private Double baseSalary;
}