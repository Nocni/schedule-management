package rs.raf.RasporedService.services;

import org.springframework.stereotype.Service;
import rs.raf.RasporedService.models.Ucionica;
import rs.raf.RasporedService.repositories.UcionicaRepository;

import java.util.Optional;

@Service
public class UcionicaService {

    private final UcionicaRepository ucionicaRepository;

    public UcionicaService(UcionicaRepository ucionicaRepository) {
        this.ucionicaRepository = ucionicaRepository;
    }

    public <S extends Ucionica> S save(S ucionica){
        return ucionicaRepository.save(ucionica);
    }

    public Optional<Ucionica> getUcionicaByOznaka(String oznaka) {
        return ucionicaRepository.findByOznaka(oznaka);
    }

    public Optional<Ucionica> getUcionicaById(int ucionicaId) {
        return ucionicaRepository.findById(ucionicaId);
    }

}
