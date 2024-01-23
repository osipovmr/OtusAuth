package otus.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UserAuthResponseDto {
    private UUID userUUID;
    private String login;
    private String firstName;
    private String lastName;
    private String email;
}
