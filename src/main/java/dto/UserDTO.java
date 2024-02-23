package dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class UserDTO {

    private String email;
    private int id;
    private String lastName;
    private String password;
    private String phone;
    private int userStatus;
    private String username;
    private String firstName;
}
