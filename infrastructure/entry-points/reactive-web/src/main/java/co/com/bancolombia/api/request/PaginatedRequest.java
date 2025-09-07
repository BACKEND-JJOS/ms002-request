package co.com.bancolombia.api.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PaginatedRequest {
    @NotNull(message = "size is required")
    @Min(value = 1, message = "size must be greater than 0")
    private Integer size;

    @NotNull(message = "page is required")
    @Min(value = 0, message = "page must be greater than or equal to 0")
    private Integer page;
}
