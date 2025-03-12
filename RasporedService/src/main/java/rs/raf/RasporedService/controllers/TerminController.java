package rs.raf.RasporedService.controllers;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.RasporedService.dtos.TerminDto;
import rs.raf.RasporedService.models.Termin;
import rs.raf.RasporedService.services.TerminService;

import java.util.List;

@RestController
@RequestMapping("/termin")
public class TerminController {

    private final TerminService terminService;

    public TerminController(TerminService terminService) {
        this.terminService = terminService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Termin dodajTermin(@RequestBody TerminDto termin) {
        return terminService.save(termin);
    }

    @DeleteMapping()
    public ResponseEntity<?> obrisiTermin(@RequestParam int pocetak,
                                          @RequestParam String ucionicaOznaka,
                                          @RequestParam String dan) {
        terminService.obrisiTermin(pocetak, ucionicaOznaka, dan);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sortiraniPoUcioniciOpadajuce")
    public ResponseEntity<List<Termin>> sortiraniPoUcioniciOpadajuce() {
        return ResponseEntity.ok(terminService.sortiraniPoUcioniciOpadajuce());
    }

    @GetMapping("/sortiraniPoUcioniciRastuce")
    public ResponseEntity<List<Termin>> sortiraniPoUcioniciRastuce() {
        return ResponseEntity.ok(terminService.sortiraniPoUcioniciRastuce());
    }

    @GetMapping("/sortiraniPoDanuOpadajuce")
    public ResponseEntity<List<Termin>> sortiraniPoDanuOpadajuce() {
        return ResponseEntity.ok(terminService.sortiraniPoDanuOpadajuce());
    }

    @GetMapping("/sortiraniPoDanuRastuce")
    public ResponseEntity<List<Termin>> sortiraniPoDanuRastuce() {
        return ResponseEntity.ok(terminService.sortiraniPoDanuRastuce());
    }

    @GetMapping("/sortiraniPoVremenuOpadajuce")
    public ResponseEntity<List<Termin>> sortiraniPoVremenuOpadajuce() {
        return ResponseEntity.ok(terminService.sortiraniPoVremenuOpadajuce());
    }

    @GetMapping("/sortiraniPoVremenuRastuce")
    public ResponseEntity<List<Termin>> sortiraniPoVremenuRastuce() {
        return ResponseEntity.ok(terminService.sortiraniPoVremenuRastuce());
    }

    @GetMapping("/poGrupi")
    public ResponseEntity<List<Termin>> poGrupi(@RequestParam String grupa) {
        return ResponseEntity.ok(terminService.poGrupi(grupa));
    }

    @GetMapping("/poNastavniku")
    public ResponseEntity<List<Termin>> poNastavniku(@RequestParam String nastavnik) {
        return ResponseEntity.ok(terminService.poNastavniku(nastavnik));
    }

    @GetMapping("/poPredmetu")
    public ResponseEntity<List<Termin>> poPredmetu(@RequestParam String predmet) {
        return ResponseEntity.ok(terminService.poPredmetu(predmet));
    }

    @GetMapping("/poDanuIUcionici")
    public ResponseEntity<List<Termin>> findAllByDanAndUcionica(@RequestParam String dan,
                                                                @RequestParam String ucionica) {
        return ResponseEntity.ok(terminService.findAllByDanAndUcionica(dan, ucionica));
    }
}
