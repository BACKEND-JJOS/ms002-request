package co.com.bancolombia.model.status;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Status {

    private Integer idStatus;
    private String name;
    private String description;

}
