package rs.raf.RasporedService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.RasporedService.dtos.NastavnikDto;
import rs.raf.RasporedService.mapper.Mapper;
import rs.raf.RasporedService.models.Nastavnik;
import rs.raf.RasporedService.repositories.NastavnikRepository;

import java.util.Optional;

@Service
public class NastavnikService {

    private final NastavnikRepository nastavnikRepository;

    private Mapper mapper;

    @Autowired
    public NastavnikService(NastavnikRepository nastavnikRepository, Mapper mapper) {
        this.nastavnikRepository = nastavnikRepository;
        this.mapper = mapper;
    }

    public NastavnikDto save(Nastavnik nastavnik){
        nastavnikRepository.save(nastavnik);
        return mapper.nastavnikToNastavnikDto(nastavnik);
    }

    public Optional<Nastavnik> getNastavnikByimeAndPrezime(String ime, String prezime) {
        return nastavnikRepository.findByImeAndPrezime(ime, prezime);
    }

    public Optional<Nastavnik> getNastavnikById(int id) {
        return nastavnikRepository.findById(id);
    }

}
