package rs.raf.AuthService.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rs.raf.AuthService.models.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("select u from User u where lower(u.username) = lower(:username) and lower(u.password) = lower(:password)")
    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("select u from User u where lower(u.username) = lower(:username)")
    Optional<User> findByUsername(String username);
}
