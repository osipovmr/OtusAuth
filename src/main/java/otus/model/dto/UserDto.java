package otus.model.dto;

import lombok.Data;

@Data
public class UserDto {
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
