package co.com.bancolombia.model.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long idUser;
    private String identityDocument;
    private String email;
}
