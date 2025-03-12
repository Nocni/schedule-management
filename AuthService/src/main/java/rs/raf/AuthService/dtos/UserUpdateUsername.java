package rs.raf.AuthService.dtos;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class UserUpdateUsername {

    @NotBlank
    private String username;

    @Length(min = 8, max = 20)
    private String password;

    @Length(min = 8, max = 20)
    private String newUsername;

}
