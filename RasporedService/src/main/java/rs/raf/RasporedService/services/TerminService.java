package rs.raf.RasporedService.services;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import rs.raf.RasporedService.dtos.TerminDto;
import rs.raf.RasporedService.messaging.NotificationMessageSender;
import rs.raf.RasporedService.models.*;
import rs.raf.RasporedService.repositories.TerminRepository;
import rs.raf.RasporedService.security.JwtUtil;

import java.util.List;
import java.util.Optional;

@Service
public class TerminService {

    private final TerminRepository terminRepository;
    private final NastavnikService nastavnikService;
    private final PredmetService predmetService;
    private final UcionicaService ucionicaService;
    private final GrupaService grupaService;
    private final NotificationMessageSender notificationSender;
    private final JwtUtil jwtUtil;

    public TerminService(TerminRepository terminRepository, NastavnikService nastavnikService,
                         PredmetService predmetService, UcionicaService ucionicaService, 
                         GrupaService grupaService, NotificationMessageSender notificationSender,
                         JwtUtil jwtUtil) {
        this.terminRepository = terminRepository;
        this.nastavnikService = nastavnikService;
        this.predmetService = predmetService;
        this.ucionicaService = ucionicaService;
        this.grupaService = grupaService;
        this.notificationSender = notificationSender;
        this.jwtUtil = jwtUtil;
    }

    public void save(Termin termin) {
        if (predmetService.getPredmetByNaziv(termin.getPredmet().getNaziv()).isEmpty())
            predmetService.save(termin.getPredmet());
        if (ucionicaService.getUcionicaByOznaka(termin.getUcionica().getOznaka()).isEmpty())
            ucionicaService.save(termin.getUcionica());
        if (nastavnikService.getNastavnikByimeAndPrezime(termin.getNastavnik().getIme(), termin.getNastavnik().getPrezime()).isEmpty())
            nastavnikService.save(termin.getNastavnik());
        terminRepository.save(termin);
    }

    public Termin save(TerminDto termin) {

        try {
            Optional<Nastavnik> nastavnikOptional = nastavnikService.getNastavnikById(termin.getNastavnik());
            Nastavnik nastavnik = nastavnikOptional.orElse(null);
            Optional<Predmet> predmetOptional = predmetService.getPredmetById(termin.getPredmet());
            Predmet predmet = predmetOptional.orElse(null);
            Optional<Ucionica> ucionicaOptional = ucionicaService.getUcionicaById(termin.getUcionica());
            Ucionica ucionica = ucionicaOptional.orElse(null);
            List<Grupa> grupe = grupaService.getGrupeByIds(termin.getGrupe());
            System.out.println(grupe + " " + ucionica + " " + predmet + " " + nastavnikOptional);

            if (nastavnik != null && predmet != null && ucionica != null && !grupe.isEmpty()) {
                Termin noviTermin = new Termin();
                noviTermin.setNastavnik(nastavnik);
                noviTermin.setPredmet(predmet);
                noviTermin.setUcionica(ucionica);
                noviTermin.setGrupe(grupe);
                noviTermin.setPocetak(Math.toIntExact(termin.getPocetak()));
                noviTermin.setKraj(Math.toIntExact(termin.getKraj()));
                noviTermin.setDan(termin.getDan());
                noviTermin.setTipNastave(termin.getTipNastave());
                nastavnik.getPredmeti().add(predmet);

                nastavnikService.save(nastavnik);
//                predmetService.save(predmet);
                Termin savedTermin = terminRepository.save(noviTermin);
                
                // Send notification for schedule change
                try {
                    // For now, using default values since we don't have user context in this method
                    // In a real implementation, this would be passed from the controller
                    notificationSender.sendScheduleChangeNotification(1L, "admin@raf.rs", 
                        String.format("Dodat je novi termin: %s - %s u %s", 
                            predmet.getNaziv(), nastavnik.getIme() + " " + nastavnik.getPrezime(), 
                            ucionica.getOznaka()), "DODAVANJE");
                } catch (Exception e) {
                    // Log error but don't fail the operation
                    System.err.println("Failed to send notification: " + e.getMessage());
                }
                
                return savedTermin;
            } else {
                throw new EntityNotFoundException("Nastavnik, predmet, ucionica ili grupe nisu pronađeni.");
            }
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Nastavnik, predmet, ucionica ili grupe nisu pronađeni.");
        }
    }

    public void obrisiTermin(int pocetak, String ucionicaOznaka, String dan) {
        Termin termin = terminRepository.findByPocetakAndUcionicaOznakaAndDan(pocetak, ucionicaOznaka, dan);
        if (termin != null) {
            terminRepository.delete(termin);
            
            // Send notification for schedule deletion
            try {
                notificationSender.sendScheduleChangeNotification(1L, "admin@raf.rs", 
                    String.format("Obrisan je termin: %s u %s", 
                        termin.getPredmet().getNaziv(), termin.getUcionica().getOznaka()), "BRISANJE");
            } catch (Exception e) {
                // Log error but don't fail the operation
                System.err.println("Failed to send notification: " + e.getMessage());
            }
        } else {
            throw new EntityNotFoundException("Termin nije pronađen.");
        }
    }

    public List<Termin> sortiraniPoUcioniciOpadajuce() {
        return terminRepository.findAllByOrderByUcionicaDesc();
    }

    public List<Termin> sortiraniPoUcioniciRastuce() {
        return terminRepository.findAllByOrderByUcionicaAsc();
    }

    public List<Termin> sortiraniPoDanuOpadajuce() {
        return terminRepository.findAllByOrderByDanDesc();
    }

    public List<Termin> sortiraniPoDanuRastuce() {
        return terminRepository.findAllByOrderByDanAsc();
    }

    public List<Termin> sortiraniPoVremenuOpadajuce() {
        return terminRepository.findAllByOrderByPocetakDesc();
    }

    public List<Termin> sortiraniPoVremenuRastuce() {
        return terminRepository.findAllByOrderByPocetakAsc();
    }

    public List<Termin> poGrupi(String grupa) {
        return terminRepository.findAllByGrupa(grupa);
    }

    public List<Termin> poNastavniku(String nastavnik) {
        return terminRepository.findAllByNastavnik(nastavnik);
    }

    public List<Termin> poPredmetu(String predmet) {
        return terminRepository.findAllByPredmet(predmet);
    }

    public List<Termin> findAllByDanAndUcionica(String dan, String ucionica) {
        return terminRepository.findAllByDanAndUcionica(dan, ucionica);
    }
}
