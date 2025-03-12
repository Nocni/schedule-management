package rs.raf.AuthService.controllers;

import javax.validation.Valid;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.AuthService.dtos.*;
import rs.raf.AuthService.security.CheckSecurity;
import rs.raf.AuthService.services.UserService;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestHeader("Authorization") String Authorization) {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> loginUser(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }

    @PostMapping(value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<UserDto> registerUser(@RequestBody @Valid UserCreateDto userCreateDto) {
        return new ResponseEntity<>(userService.register(userCreateDto), HttpStatus.CREATED);
    }

    @CheckSecurity(roles = {"ROLE_ADMIN"})
    @PostMapping(value = "/registerProfessor", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> registerProfessor(@RequestHeader("Authorization") String Authorization,
                                                    @RequestBody @Valid ProfessorDto professorDto) throws JsonProcessingException {
        return new ResponseEntity<>(userService.registerProfessor(professorDto), HttpStatus.CREATED);
    }

    @PutMapping("/updatePassword")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<String> updatePassword(@RequestHeader("Authorization") String Authorization,
                                                 @RequestBody @Valid UserUpdatePassword password) {
        return new ResponseEntity<>(userService.updatePassword(password), HttpStatus.OK);
    }

    @PutMapping("/updateUsername")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<String> updateUsername(@RequestHeader("Authorization") String Authorization,
                                                 @RequestBody @Valid UserUpdateUsername username) {
        return new ResponseEntity<>(userService.updateUsername(username), HttpStatus.OK);
    }

    @PutMapping("/updateEmail")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<String> updateEmail(@RequestHeader("Authorization") String Authorization,
                                              @RequestBody @Valid UserUpdateEmail email) {
        return new ResponseEntity<>(userService.updateEmail(email), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteUser(@RequestHeader("Authorization") String Authorization,
                                           @PathVariable("id") int id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/assignRole/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<UserDto> assignRole(@RequestHeader("Authorization") String Authorization,
                                              @PathVariable("id") int userId,
                                              @RequestParam String roleName) {
        return new ResponseEntity<>(userService.assignRole(userId, roleName), HttpStatus.OK);
    }

}
