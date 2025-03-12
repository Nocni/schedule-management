package rs.raf.RasporedService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ime", "prezime"}))
public class Nastavnik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int nastavnikId;

    private String ime;

    private String prezime;

    private String zvanje;

    private String email;

    @OneToMany(mappedBy = "nastavnik", fetch = FetchType.EAGER)
    private List<Predmet> predmeti;

}
