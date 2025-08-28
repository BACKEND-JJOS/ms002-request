package co.com.bancolombia.consumer.response.commons;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApiResponse<T> {

    private T data;
    private Integer status;
    private String message;
}
