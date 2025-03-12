package rs.raf.RasporedService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "termini", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"nastavnik", "dan", "pocetak", "kraj"}),
        @UniqueConstraint(columnNames = {"ucionica", "dan", "pocetak", "kraj"}),
        @UniqueConstraint(columnNames = {"grupe", "dan", "pocetak", "kraj"})
})
public class Termin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int terminId;

    @ManyToOne
    @JoinColumn(name = "predmet")
    private Predmet predmet;

    private String tipNastave;

    @ManyToOne
    @JoinColumn(name = "nastavnik")
    private Nastavnik nastavnik;

    @ManyToMany
    @JoinTable(name = "termin_grupa",
            joinColumns = @JoinColumn(name = "terminId"),
            inverseJoinColumns = @JoinColumn(name = "grupaId"))
    private List<Grupa> grupe;

    private String dan;

    private int pocetak;

    private int kraj;

    @ManyToOne
    @JoinColumn(name = "ucionica")
    private Ucionica ucionica;

}

