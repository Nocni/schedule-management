package rs.raf.RasporedService.csvparser;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import rs.raf.RasporedService.dtos.TerminDto;
import rs.raf.RasporedService.models.*;
import rs.raf.RasporedService.services.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

@Component
public class CsvToH2Database implements CommandLineRunner {

    private final NastavnikService nastavnikService;

    private final UcionicaService ucionicaService;

    private final PredmetService predmetService;

    private final GrupaService grupaService;

    private final TerminService terminService;


    public CsvToH2Database(NastavnikService nastavnikService, UcionicaService ucionicaService, PredmetService predmetService, GrupaService grupaService, TerminService terminService) {
        this.nastavnikService = nastavnikService;
        this.ucionicaService = ucionicaService;
        this.predmetService = predmetService;
        this.grupaService = grupaService;
        this.terminService = terminService;
    }

    @Override
    public void run(String... args) {
        String csvFilePath = "src/main/resources/raspored.csv";

        try (CSVReader reader = new CSVReader(new FileReader(csvFilePath))) {
            String[] header = reader.readNext();

            if (header != null) {
                int columnIndex1 = 0;
                int columnIndex2 = 1;
                int columnIndex3 = 2;
                int columnIndex4 = 3;
                int columnIndex5 = 4;
                int columnIndex6 = 5;
                int columnIndex7 = 6;

                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {

                    String predmet = nextLine[columnIndex1];
                    String tip = nextLine[columnIndex2];
                    String nastavnik = nextLine[columnIndex3];
                    String grupe = nextLine[columnIndex4];
                    String dan = nextLine[columnIndex5].trim();
                    String termin = nextLine[columnIndex6];
                    String ucionica = nextLine[columnIndex7];

                    Ucionica novaUcionica = new Ucionica();
                    novaUcionica.setOznaka(ucionica.split(" ")[0]);
                    novaUcionica.setKapacitet(20);
                    novaUcionica.setRacunari(true);
                    try {
                        if (ucionicaService.getUcionicaByOznaka(novaUcionica.getOznaka()).isEmpty()){
                            ucionicaService.save(novaUcionica);
                        }
                    } catch (DataIntegrityViolationException ignored) {
                    }
                    String[] predmetNaziv = predmet.split(" ");
                    StringBuilder sb = new StringBuilder();
                    for (String s : predmetNaziv) {
                        if (!s.isEmpty()){
                            sb.append(s.charAt(0));
                        }
                    }
                    String sifra = sb.toString().toUpperCase();
                    Predmet noviPredmet = new Predmet();
                    noviPredmet.setNaziv(predmet);
                    noviPredmet.setSifra(sifra);
                    noviPredmet.setBrojESPB(6);
                    try {
                        noviPredmet.setNastavnik(nastavnikService.getNastavnikByimeAndPrezime(nastavnik.split(" ")[1], nastavnik.split(" ")[0]).orElse(null));
                        if (predmetService.getPredmetByNaziv(predmet).isEmpty()){
                            predmetService.save(noviPredmet);
                        }
                    } catch (DataIntegrityViolationException ignored) {
                    }
                    Nastavnik noviNastavnik = new Nastavnik();
                    noviNastavnik.setIme(nastavnik.split(" ")[1]);
                    noviNastavnik.setPrezime(nastavnik.split(" ")[0]);
                    noviNastavnik.setZvanje("Profesor");
                    noviNastavnik.setEmail(nastavnik.split(" ")[0].toLowerCase(Locale.ROOT).concat(nastavnik.split(" ")[1].toLowerCase(Locale.ROOT).concat("@raf.rs")));
                    noviNastavnik.setPredmeti(new ArrayList<>());
                    noviNastavnik.getPredmeti().add(noviPredmet);

                    try {
                        if (nastavnikService.getNastavnikByimeAndPrezime(noviNastavnik.getIme(), noviNastavnik.getPrezime()).isEmpty()){
                            nastavnikService.save(noviNastavnik);
                        }
                    } catch (DataIntegrityViolationException ignored) {
                    }
                    String[] noveGrupe = grupe.split(",");
                    for (String grupa : noveGrupe) {
                        Grupa novaGrupa = new Grupa();
                        novaGrupa.setOznaka(grupa.trim());
                        novaGrupa.setTermini(new ArrayList<>());
                        try {
                            if (grupaService.getGrupaByOznaka(novaGrupa.getOznaka()).isEmpty()){
                                grupaService.save(novaGrupa);
                            }
                        } catch (DataIntegrityViolationException ignored) {
                        }
                    }
                    String[] vreme = termin.split("-");
                    String pocetakString = (vreme[0].substring(0, 2));
                    int pocetak;
                    if (pocetakString.charAt(1) == ':') {
                        pocetak = Integer.parseInt(vreme[0].substring(0, 1));
                    } else pocetak = Integer.parseInt(pocetakString);
                    int kraj = Integer.parseInt(vreme[1]);
                    TerminDto noviTermin = new TerminDto();
                    noviTermin.setDan(dan.trim());
                    noviTermin.setPocetak(pocetak);
                    noviTermin.setKraj(kraj);
                    noviTermin.setTipNastave(tip);
                    noviTermin.setPredmet(predmetService.getPredmetByNaziv(predmet).get().getPredmetId());
                    noviTermin.setUcionica(ucionicaService.getUcionicaByOznaka(ucionica.split(" ")[0]).get().getUcionicaId());
                    noviTermin.setGrupe(new ArrayList<>());
                    noviTermin.setNastavnik(nastavnikService.getNastavnikByimeAndPrezime(noviNastavnik.getIme(), noviNastavnik.getPrezime()).get().getNastavnikId());
                    for (String grupa : noveGrupe) {
                        noviTermin.getGrupe().add(grupaService.getGrupaByOznaka(grupa.trim()).get().getGrupaId());
                    }
                    try {
                        terminService.save(noviTermin);
                        noviPredmet.setNastavnik(nastavnikService.getNastavnikByimeAndPrezime(noviNastavnik.getIme(), noviNastavnik.getPrezime()).orElse(null));
                        predmetService.save(noviPredmet);
                    } catch (DataIntegrityViolationException ignored) {
                    }
                }
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
