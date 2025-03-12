package rs.raf.RasporedService.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf.RasporedService.models.Grupa;

import java.util.Optional;

public interface GrupaRepository extends CrudRepository<Grupa, Integer> {

    @Query("select g from Grupa g where lower(g.oznaka) = lower(:oznaka)")
    Optional<Grupa> findByOznaka(String oznaka);

}
