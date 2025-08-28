package co.com.bancolombia.model.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Integer idUser;
    private String identityDocument;
    private String email;
}
