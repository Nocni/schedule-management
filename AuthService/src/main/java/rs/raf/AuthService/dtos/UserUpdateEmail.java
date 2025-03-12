package rs.raf.AuthService.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserUpdateEmail {

    @NotBlank
    private String username;

    @Length(min = 8, max = 20)
    private String password;

    @Email
    private String newEmail;

}
