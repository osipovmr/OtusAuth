package otus.model.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
    private String login;
    private String password;
    @JsonProperty("firstName")
    @JsonAlias("first_name")
    private String firstName;
    @JsonProperty("lastName")
    @JsonAlias("last_name")
    private String lastName;
    private String email;
    private String phone;
}
