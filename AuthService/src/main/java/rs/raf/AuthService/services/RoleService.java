package rs.raf.AuthService.services;

import org.springframework.stereotype.Service;
import rs.raf.AuthService.models.Role;
import rs.raf.AuthService.models.User;
import rs.raf.AuthService.repositories.RoleRepository;
import rs.raf.AuthService.repositories.UserRepository;

import java.util.List;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public <S extends Role> S add(S role){
        return roleRepository.save(role);
    }

    public void delete(int id) {
        List<User> users = roleRepository.findById(id).get().getUsers();
        for (User user : users) {
            user.getRoles().removeIf(role -> role.getRoleId() == id);
            userRepository.save(user);
        }
        roleRepository.deleteById(id);
    }
}
