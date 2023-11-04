package otus.model.dto;

import lombok.Data;

@Data
public class UserInfoDto {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
}
