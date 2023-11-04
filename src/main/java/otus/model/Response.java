package otus.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class Response {
    private String status;
}
