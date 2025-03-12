package rs.raf.RasporedService.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import rs.raf.RasporedService.models.Termin;

import java.util.List;

public interface TerminRepository extends CrudRepository<Termin, Integer> {

    @Query("select t from Termin t order by t.ucionica.oznaka desc ")
    List<Termin> findAllByOrderByUcionicaDesc ();

    @Query("select t from Termin t order by t.ucionica.oznaka asc ")
    List<Termin> findAllByOrderByUcionicaAsc ();

    @Query("select t from Termin t order by t.dan asc ")
    List<Termin> findAllByOrderByDanAsc();

    @Query("select t from Termin t order by t.dan desc ")
    List<Termin> findAllByOrderByDanDesc();

    @Query("select t from Termin t order by t.pocetak asc ")
    List<Termin> findAllByOrderByPocetakAsc();

    @Query("select t from Termin t order by t.pocetak desc ")
    List<Termin> findAllByOrderByPocetakDesc();

    @Query("select t from Termin t where "
            + "(:dan is null or lower(t.dan) like lower(:dan)) and "
            + "(:ucionicaOznaka is null or lower(t.ucionica.oznaka) like lower(:ucionicaOznaka))")
    List<Termin> findAllByDanAndUcionica(String dan, String ucionicaOznaka);

    @Query("select t from Termin t join t.grupe g where lower(g.oznaka) = lower(:grupa)")
    List<Termin> findAllByGrupa(String grupa);

    @Query("select t from Termin t where lower(t.nastavnik.ime) = lower(:nastavnik)")
    List<Termin> findAllByNastavnik(String nastavnik);

    @Query("select t from Termin t where lower(t.predmet.naziv) = lower(:predmet)")
    List<Termin> findAllByPredmet(String predmet);

    @Query("select t from Termin t where "
            + "(:pocetak is null or t.pocetak = :pocetak) and "
            + "(:ucionicaOznaka is null or lower(t.ucionica.oznaka) like lower(:ucionicaOznaka)) and "
            + "(:dan is null or lower(t.dan) like lower(:dan))")
    Termin findByPocetakAndUcionicaOznakaAndDan(int pocetak, String ucionicaOznaka, String dan);

}
