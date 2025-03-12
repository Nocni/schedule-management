package rs.raf.RasporedService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Predmet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int predmetId;

    @Column(unique = true)
    private String naziv;

    private String sifra;

    private int brojESPB;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "nastavnik")
    private Nastavnik nastavnik;
}
