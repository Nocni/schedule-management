package rs.raf.RasporedService.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf.RasporedService.models.Nastavnik;

import java.util.Optional;

public interface NastavnikRepository extends CrudRepository<Nastavnik, Integer> {

    @Query("select n from Nastavnik n left join fetch n.predmeti where "
            + "(:ime is null or lower(n.ime) like lower(:ime)) and "
            + "(:prezime is null or lower(n.prezime) like lower(:prezime))")
    Optional<Nastavnik> findByImeAndPrezime(String ime, String prezime);

    @Query("select n from Nastavnik n where lower(n.email) like lower(:email)")
    Optional<Nastavnik> findNastavnikByEmail(String email);

}
