package otus.model.dto;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
