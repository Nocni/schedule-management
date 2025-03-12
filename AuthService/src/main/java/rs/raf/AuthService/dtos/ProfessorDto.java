package rs.raf.AuthService.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import rs.raf.AuthService.models.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
public class ProfessorDto {

    @NotBlank
    private String ime;

    @NotBlank
    private String prezime;

    private String zvanje;

    @Email
    private String email;

    @NotBlank
    private String username;

    @Length(min = 8, max = 20)
    private String password;

    private List<Role> roles;

}
