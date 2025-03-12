package rs.raf.RasporedService.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.RasporedService.models.Grupa;
import rs.raf.RasporedService.models.Nastavnik;
import rs.raf.RasporedService.models.Predmet;
import rs.raf.RasporedService.models.Ucionica;
import rs.raf.RasporedService.services.GrupaService;
import rs.raf.RasporedService.services.NastavnikService;
import rs.raf.RasporedService.services.PredmetService;
import rs.raf.RasporedService.services.UcionicaService;

import java.util.Optional;

@RestController
@RequestMapping("/get")
public class GetController {

    private final NastavnikService nastavnikService;

    private final PredmetService predmetService;

    private final UcionicaService ucionicaService;

    private final GrupaService grupaService;


    public GetController(NastavnikService nastavnikService, PredmetService predmetService, UcionicaService ucionicaService, GrupaService grupaService) {
        this.nastavnikService = nastavnikService;
        this.predmetService = predmetService;
        this.ucionicaService = ucionicaService;
        this.grupaService = grupaService;
    }

    @GetMapping(value = "/nastavnik", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getNastavnik(@RequestParam String ime, @RequestParam String prezime) {
        Optional<Nastavnik> optionalNastavnik =  nastavnikService.getNastavnikByimeAndPrezime(ime, prezime);
        if(optionalNastavnik.isPresent()) {
            return ResponseEntity.ok(optionalNastavnik.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/predmet", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getPredmet(@RequestParam String naziv) {
        Optional<Predmet> optionalPredmet =  predmetService.getPredmetByNaziv(naziv);
        if(optionalPredmet.isPresent()) {
            return ResponseEntity.ok(optionalPredmet.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/ucionica", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUcionica(@RequestParam String oznaka) {
        Optional<Ucionica> optionalUcionica =  ucionicaService.getUcionicaByOznaka(oznaka);
        if(optionalUcionica.isPresent()) {
            return ResponseEntity.ok(optionalUcionica.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/grupa", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getGrupa(@RequestParam String oznaka) {
        Optional<Grupa> optionalGrupa =  grupaService.getGrupaByOznaka(oznaka);
        if(optionalGrupa.isPresent()) {
            return ResponseEntity.ok(optionalGrupa.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
