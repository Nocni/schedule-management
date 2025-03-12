package rs.raf.AuthService.controllers;

import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.AuthService.models.Role;
import rs.raf.AuthService.security.CheckSecurity;
import rs.raf.AuthService.services.RoleService;

@RestController
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/new")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Role> newRole(@RequestHeader("Authorization") String Authorization,
                                        @RequestBody @Valid Role Role) {
        return new ResponseEntity<>(roleService.add(Role), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    @CheckSecurity(roles = {"ROLE_ADMIN"})
    public ResponseEntity<Void> deleteRole(@RequestHeader("Authorization") String Authorization,
                                           @PathVariable("id") int id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
