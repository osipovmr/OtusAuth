package otus.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserAuthResponseDto {
    private int id;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
}
