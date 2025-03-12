package rs.raf.RasporedService.services;

import org.springframework.stereotype.Service;
import rs.raf.RasporedService.models.Predmet;
import rs.raf.RasporedService.repositories.PredmetRepository;

import java.util.Optional;

@Service
public class PredmetService {

    private final PredmetRepository predmetRepository;



    public PredmetService(PredmetRepository predmetRepository) {
        this.predmetRepository = predmetRepository;
    }

    public Predmet save(Predmet predmet){
        return predmetRepository.save(predmet);
    }

    public Optional<Predmet> getPredmetByNaziv(String naziv) {
        return predmetRepository.findByNaziv(naziv);
    }

    public Optional<Predmet> getPredmetById(int predmetId) {
        return predmetRepository.findById(predmetId);
    }
}
