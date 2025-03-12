package rs.raf.AuthService.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.AuthService.models.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {

    @Query("select r from Role r where lower(r.name) = lower(:name)")
    Optional<Role> findRoleByName(String name);

}
