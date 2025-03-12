package rs.raf.AuthService.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rs.raf.AuthService.models.Role;
import rs.raf.AuthService.models.User;
import rs.raf.AuthService.repositories.RoleRepository;
import rs.raf.AuthService.repositories.UserRepository;

import java.util.ArrayList;

@Profile({"default"})
@Component
public class BootstrapData implements CommandLineRunner {

    private RoleRepository roleRepository;
    private UserRepository userRepository;

    public BootstrapData(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        //Insert roles
        Role roleUser = new Role("ROLE_USER", "User role");
        Role roleAdmin = new Role("ROLE_ADMIN", "Admin role");
        Role roleProfessor = new Role("ROLE_PROFESSOR", "Professor role");
        roleRepository.save(roleUser);
        roleRepository.save(roleAdmin);
        roleRepository.save(roleProfessor);
        //Insert admin and user
        User admin = new User();
        admin.setEmail("admin@gmail.com");
        admin.setUsername("admin");
        admin.setPassword("admin1234");
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setRoles(new ArrayList<>());
        admin.getRoles().add(roleAdmin);
        User user = new User();
        user.setEmail("user@email");
        user.setUsername("user");
        user.setPassword("user1234");
        user.setFirstName("User");
        user.setLastName("User");
        user.setRoles(new ArrayList<>());
        user.getRoles().add(roleUser);
        User professor = new User();
        professor.setEmail("jovanovicirena@raf.rs");
        professor.setUsername("professor");
        professor.setPassword("professor1234");
        professor.setFirstName("Irena");
        professor.setLastName("Jovanovic");
        professor.setRoles(new ArrayList<>());
        professor.getRoles().add(roleProfessor);
        userRepository.save(admin);
        userRepository.save(user);
        userRepository.save(professor);
    }
}
