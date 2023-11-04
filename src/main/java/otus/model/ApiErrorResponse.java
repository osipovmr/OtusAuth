package otus.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;

    @Builder
    public ApiErrorResponse(int status, String error) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
    }
}
