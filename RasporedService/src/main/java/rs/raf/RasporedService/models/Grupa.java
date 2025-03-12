package rs.raf.RasporedService.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Grupa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int grupaId;

    @Column(unique = true)
    private String oznaka;

    @JsonIgnore
    @ManyToMany(mappedBy = "grupe")
    private List<Termin> termini;

}
