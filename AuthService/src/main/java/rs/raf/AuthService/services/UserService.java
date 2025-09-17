package rs.raf.AuthService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import rs.raf.AuthService.dtos.*;
import rs.raf.AuthService.mappers.UserMapper;
import rs.raf.AuthService.models.Role;
import rs.raf.AuthService.models.User;
import rs.raf.AuthService.repositories.RoleRepository;
import rs.raf.AuthService.repositories.UserRepository;
import rs.raf.AuthService.security.service.TokenService;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    private TokenService tokenService;

    private UserMapper userMapper;

    private RoleRepository roleRepository;

    private RestTemplate rasporedServiceRestTemplate;


    public UserService(UserRepository userRepository, TokenService tokenService, UserMapper userMapper, RoleRepository roleRepository, RestTemplate rasporedServiceRestTemplate) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.rasporedServiceRestTemplate = rasporedServiceRestTemplate;
    }

    public TokenResponseDto login(TokenRequestDto tokenRequestDto) {
        User user = userRepository.
                findByUsernameAndPassword(tokenRequestDto.getUsername(), tokenRequestDto.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        
        Claims claims = Jwts.claims();
        claims.put("id", user.getUserId());
        claims.put("username", user.getUsername());
        claims.put("email", user.getEmail());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        
        // Handle multiple roles - for now we'll put the first role found
        // In a more complex system, you might want to handle multiple roles differently
        String primaryRole = "ROLE_USER"; // Default role
        if (!user.getRoles().isEmpty()) {
            primaryRole = user.getRoles().get(0).getName();
        }
        claims.put("role", primaryRole);
        
        // Add all roles as a list for more complex authorization if needed
        String[] roleNames = user.getRoles().stream()
                .map(Role::getName)
                .toArray(String[]::new);
        claims.put("roles", roleNames);
        
        claims.setSubject(user.getUsername());
        
        return new TokenResponseDto(tokenService.generate(claims));
    }

    public UserDto register(UserCreateDto userCreateDto) {
        User user = userMapper.userCreateDtoToUser(userCreateDto);
        userRepository.save(user);
        return userMapper.userToUserDto(user);
    }

    public String registerProfessor(ProfessorDto professorDto) throws JsonProcessingException {
        ProfessorCreateDto professorCreateDto = userMapper.professorDtoToProfessorCreateDto(professorDto);
        ResponseEntity<ProfessorRasporedDto> professorCreateDtoResponseEntity = null;
        HttpEntity<ProfessorCreateDto> request = new HttpEntity<>(professorCreateDto);
        try {
            professorCreateDtoResponseEntity = rasporedServiceRestTemplate.postForEntity("http://localhost:8080/api/raspored/new/nastavnik",
                    request,
                    ProfessorRasporedDto.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.BAD_REQUEST)) {
                throw new RuntimeException("Invalid credentials");
            }
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }

        User user = userMapper.professorDtoToUser(professorDto);
        if (professorCreateDtoResponseEntity != null && professorCreateDtoResponseEntity.getBody() != null) {
            userRepository.save(user);
        }
        return "Professor registered";
    }

    public void delete(int id) {
        userRepository.deleteById(id);
    }

    public UserDto assignRole(int userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        Role role = roleRepository.findRoleByName(roleName)
                .orElseThrow(() -> new RuntimeException("Invalid role"));
        user.getRoles().add(role);
        return userMapper.userToUserDto(userRepository.save(user));
    }

    public String updatePassword(UserUpdatePassword userUpdatePassword) {
        User user = userRepository.findByUsernameAndPassword(userUpdatePassword.getUsername(), userUpdatePassword.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        user.setPassword(userUpdatePassword.getNewPassword());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return "Password not updated";
        }
        return "Password updated";
    }

    public String updateUsername(UserUpdateUsername userUpdateUsername) {
        User user = userRepository.findByUsernameAndPassword(userUpdateUsername.getUsername(), userUpdateUsername.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        user.setUsername(userUpdateUsername.getNewUsername());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return "Username not updated";
        }
        return "Username updated";
    }

    public String updateEmail(UserUpdateEmail userUpdateEmail) {
        User user = userRepository.findByUsernameAndPassword(userUpdateEmail.getUsername(), userUpdateEmail.getPassword())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
        user.setEmail(userUpdateEmail.getNewEmail());
        try {
            userRepository.save(user);
        } catch (Exception e) {
            return "Email not updated";
        }
        return "Email updated";
    }

    public List<UserDto> findAll() {
        return userMapper.userListToUserDtoList((List<User>) userRepository.findAll());
    }

}
