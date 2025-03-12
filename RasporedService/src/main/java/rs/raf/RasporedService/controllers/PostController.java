package rs.raf.RasporedService.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.RasporedService.dtos.NastavnikDto;
import rs.raf.RasporedService.models.Grupa;
import rs.raf.RasporedService.models.Nastavnik;
import rs.raf.RasporedService.models.Predmet;
import rs.raf.RasporedService.models.Ucionica;
import rs.raf.RasporedService.services.GrupaService;
import rs.raf.RasporedService.services.NastavnikService;
import rs.raf.RasporedService.services.PredmetService;
import rs.raf.RasporedService.services.UcionicaService;

@RestController
@RequestMapping("/new")
public class PostController {


    private final NastavnikService nastavnikService;

    private final PredmetService predmetService;

    private final GrupaService grupaService;

    private final UcionicaService ucionicaService;

    public PostController(NastavnikService nastavnikService, PredmetService predmetService, GrupaService grupaService, UcionicaService ucionicaService) {
        this.nastavnikService = nastavnikService;
        this.predmetService = predmetService;
        this.grupaService = grupaService;
        this.ucionicaService = ucionicaService;
    }

    @PostMapping(value = "/nastavnik", consumes = "application/json", produces = "application/json")
    public NastavnikDto dodajNastavnika(@RequestBody Nastavnik nastavnik) { return nastavnikService.save(nastavnik); }

    @PostMapping(value = "/predmet", consumes = "application/json")
    public Predmet dodajPredmet(@RequestBody Predmet predmet) { return predmetService.save(predmet); }

    @PostMapping(value = "/grupa", consumes = "application/json")
    public Grupa dodajGrupu(@RequestBody Grupa grupa) {
        return grupaService.save(grupa);
    }

    @PostMapping(value = "/ucionica", consumes = "application/json")
    public Ucionica dodajUcionicu(@RequestBody Ucionica ucionica) {
        return ucionicaService.save(ucionica);
    }

}
