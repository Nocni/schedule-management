package rs.raf.AuthService.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.AuthService.dtos.TokenRequestDto;
import rs.raf.AuthService.dtos.TokenResponseDto;
import rs.raf.AuthService.dtos.UserDto;
import rs.raf.AuthService.models.Role;
import rs.raf.AuthService.security.CheckSecurity;
import rs.raf.AuthService.services.RoleService;
import rs.raf.AuthService.services.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final RoleService roleService;

    public AuthController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * POST /auth/prijava: Autentifikacija korisnika i generisanje JWT tokena.
     */
    @PostMapping("/prijava")
    public ResponseEntity<TokenResponseDto> prijava(@RequestBody @Valid TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.login(tokenRequestDto), HttpStatus.OK);
    }

    /**
     * POST /auth/uloge: Dodavanje novih uloga.
     */
    @PostMapping("/uloge")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Role> uloge(@RequestHeader("Authorization") String authorization,
                                      @RequestBody @Valid Role role) {
        return new ResponseEntity<>(roleService.add(role), HttpStatus.CREATED);
    }

    /**
     * POST /auth/dodela-uloge: Pridruživanje uloga korisnicima.
     */
    @PostMapping("/dodela-uloge")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<UserDto> dodelaUloge(@RequestHeader("Authorization") String authorization,
                                               @RequestParam int userId,
                                               @RequestParam String roleName) {
        return new ResponseEntity<>(userService.assignRole(userId, roleName), HttpStatus.OK);
    }

}