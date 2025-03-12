package rs.raf.RasporedService.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TerminDto {
    private int nastavnik;
    private int predmet;
    private int ucionica;
    private int pocetak;
    private int kraj;
    private String dan;
    private String tipNastave;
    private List<Integer> grupe;
}
