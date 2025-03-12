package rs.raf.RasporedService.mapper;

import org.springframework.stereotype.Component;
import rs.raf.RasporedService.dtos.NastavnikDto;
import rs.raf.RasporedService.models.Nastavnik;
import rs.raf.RasporedService.repositories.GrupaRepository;
import rs.raf.RasporedService.repositories.NastavnikRepository;
import rs.raf.RasporedService.repositories.PredmetRepository;
import rs.raf.RasporedService.repositories.UcionicaRepository;

@Component
public class Mapper {

    public NastavnikDto nastavnikToNastavnikDto(Nastavnik nastavnik) {
        NastavnikDto nastavnikDto = new NastavnikDto();
        nastavnikDto.setIme(nastavnik.getIme());
        nastavnikDto.setPrezime(nastavnik.getPrezime());
        return nastavnikDto;
    }

}
