package rs.raf.RasporedService.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf.RasporedService.models.Ucionica;

import java.util.Optional;

public interface UcionicaRepository extends CrudRepository<Ucionica, Integer> {

    @Query("select u from Ucionica u where "
            + "(:oznaka is null or lower(u.oznaka) like lower(:oznaka))")
    Optional<Ucionica> findByOznaka (String oznaka);

}
