package rs.raf.AuthService.mappers;

import org.springframework.stereotype.Component;
import rs.raf.AuthService.dtos.ProfessorCreateDto;
import rs.raf.AuthService.dtos.ProfessorDto;
import rs.raf.AuthService.dtos.UserCreateDto;
import rs.raf.AuthService.dtos.UserDto;
import rs.raf.AuthService.models.User;
import rs.raf.AuthService.repositories.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private RoleRepository roleRepository;

    public UserMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UserDto userToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User userCreateDtoToUser(UserCreateDto userCreateDto) {
        User user = new User();
        user.setUsername(userCreateDto.getUsername());
        user.setPassword(userCreateDto.getPassword());
        user.setEmail(userCreateDto.getEmail());
        user.setRoles(new ArrayList<>());
        user.getRoles().add(roleRepository.findRoleByName("ROLE_USER").get());
        return user;
    }

    public List<UserDto> userListToUserDtoList(List<User> users) {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            userDtos.add(userToUserDto(user));
        }
        return userDtos;
    }

    public User professorDtoToUser(ProfessorDto professorDto) {
        User user = new User();
        user.setUsername(professorDto.getUsername());
        user.setPassword(professorDto.getPassword());
        user.setEmail(professorDto.getEmail());
        user.setRoles(new ArrayList<>());
        user.getRoles().add(roleRepository.findRoleByName("ROLE_PROFESSOR").get());
        return user;
    }

    public ProfessorCreateDto professorDtoToProfessorCreateDto(ProfessorDto professorDto) {
        ProfessorCreateDto professorCreateDto = new ProfessorCreateDto();
        professorCreateDto.setIme(professorDto.getIme());
        professorCreateDto.setPrezime(professorDto.getPrezime());
        professorCreateDto.setZvanje(professorDto.getZvanje());
        professorCreateDto.setEmail(professorDto.getEmail());
        return professorCreateDto;
    }
}
