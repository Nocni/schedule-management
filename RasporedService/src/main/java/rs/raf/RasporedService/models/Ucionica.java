package rs.raf.RasporedService.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Ucionica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ucionicaId;

    @Column(unique = true)
    private String oznaka;

    private int kapacitet;

    private boolean racunari;

}
