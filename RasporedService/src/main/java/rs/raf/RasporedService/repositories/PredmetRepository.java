package rs.raf.RasporedService.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf.RasporedService.models.Predmet;

import java.util.Optional;

public interface PredmetRepository extends CrudRepository<Predmet, Integer> {

    @Query("select p from Predmet p where "
            + "(:naziv is null or lower(p.naziv) like lower(:naziv))")
    Optional<Predmet> findByNaziv(String naziv);

}
