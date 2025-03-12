package rs.raf.RasporedService.services;

import org.springframework.stereotype.Service;
import rs.raf.RasporedService.models.Grupa;
import rs.raf.RasporedService.repositories.GrupaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class GrupaService {

    private final GrupaRepository grupaRepository;

    public GrupaService(GrupaRepository grupaRepository) {
        this.grupaRepository = grupaRepository;
    }

    public <S extends Grupa> S save(S grupa){
        return grupaRepository.save(grupa);
    }

    public List<Grupa> getGrupeByIds(List<Integer> ids) {
        return (List<Grupa>) grupaRepository.findAllById(ids);
    }

    public Optional<Grupa> getGrupaByOznaka(String oznaka) {
        return grupaRepository.findByOznaka(oznaka);
    }
}
