package com.example.progettoingegneria;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

public class TestManagementSystem {

    String resourcePath = System.getProperty("user.dir") + "/src/test/resources/com/example/progettoingegneria";
    Faker faker = new Faker();
    File dipendentiFile = Paths.get(resourcePath, "dipendenti.json").toFile();
    File lavoratoriFile = Paths.get(resourcePath, "lavoratori.json").toFile();
    File adminFile = Paths.get(resourcePath, "admin.json").toFile();

    @BeforeEach
    @AfterEach
    void setUp() throws IOException {
        List<String> emptyJSON = Arrays.asList("[]");
        List<String> initAdminJSON = Arrays.asList(
            "[",
            "    {",
            "    \"nome\":\"mario\",",
            "    \"cognome\":\"rossi\",",
            "    \"luogoNascita\":\"luogo nascita\",",
            "    \"dataNascita\":\"2000-04-28\",",
            "    \"nazionalita\":\"italiana\",",
            "    \"indirizzoEmail\":\"nome@example.com\",",
            "    \"numeroTelefono\":\"045045045\",",
            "    \"tipo\":\"admin\",",
            "    \"username\":\"mario\",",
            "    \"password\":\"secret\"",
            "    }",
            "]"
        );
        Files.write(dipendentiFile.toPath(), emptyJSON, StandardCharsets.UTF_8);
        Files.write(lavoratoriFile.toPath(), emptyJSON, StandardCharsets.UTF_8);
        Files.write(adminFile.toPath(), initAdminJSON, StandardCharsets.UTF_8);
    }

    @Test
    void testOneInstance() throws IOException, URISyntaxException {
        // se provo a creare due istanze mi aspetto di crearne una sola
        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);
        ManagementSystem ms2 = ManagementSystem.getInstance(resourcePath);
        assertEquals(ms, ms2);
    }

    @Test
    void testLoginAndLogout() throws IOException, URISyntaxException {
        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);

        // all'inizio non dovrebbero esserci utenti autenticati
        assertNull(ms.getLoggedInUser());

        // provo a fare il logout senza entrare
        assertFalse(ms.logout());

        // provo a entrare inserendo valori nulli al posto di inserire username e password: dovrebbe dare errore
        assertThrows(IllegalArgumentException.class, () -> ms.login(null, null));

        // provo ad entrare con password errate: non dovrei riuscire ad accedere e quindi nessun utente e' autenticato
        assertFalse(ms.login("", ""));
        assertNull(ms.getLoggedInUser());

        // provo a inserire username e password di un utente esistente: dovrei essere in grado di entrare
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // provo a fare il logout: non dovrebbe essere piu' autenticato nessuno
        assertTrue(ms.logout());
        assertNull(ms.getLoggedInUser());
    }

    @Test
    void testAddAndRemoveLavoratori() throws IOException, URISyntaxException {

        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);

        Lavoratore invalidData = Lavoratore.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            faker.phoneNumber().phoneNumber(),
            faker.address().streetAddress(),
            List.of(),
            List.of(),
            List.of(),
            false,
            List.of(),
            List.of(),
            faker.idNumber().ssnValid()
        );

        // crea data attuale e sottraici 20 anni
        Date twentyYearsAgo = new Date();
        twentyYearsAgo.setYear(twentyYearsAgo.getYear()-20);

        Lavoratore validData = Lavoratore.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            faker.phoneNumber().phoneNumber(),
            faker.address().streetAddress(),
            List.of(),
            List.of(),
            List.of(),
            false,
            List.of(),
            List.of(
                RecapitoUrgenza.of(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.phoneNumber().phoneNumber(),
                    faker.internet().emailAddress()
                )
            ),
            faker.idNumber().ssnValid()
        );

        // all'inizio non dovrebbe esserci nessun lavoratore
        assertEquals(0, ms.getLavoratori().size());

        // aggiungo un lavoratore nullo: lancia un errore, non dovrebbe essere inserito
        assertThrows(IllegalArgumentException.class, () -> ms.addLavoratore(null));
        assertEquals(0, ms.getLavoratori().size());

        // aggiungo un lavoratore senza accedere: non dovrebbe essere inserito
        ManagementSystemResponse response = ms.addLavoratore(invalidData);
        assertEquals(ManagementSystemStatus.NOT_LOGGED_IN, response.getStatus());
        assertEquals(0, ms.getLavoratori().size());

        // effettuo il login
        assertNull(ms.getLoggedInUser());
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // aggiungo un lavoratore con dati non validi: non dovrebbe essere inserito
        response = ms.addLavoratore(invalidData);
        assertEquals(0, ms.getLavoratori().size());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        System.out.println(response.getDetails());

        // aggiungo un lavoratore con dati validi: dovrebbe essere stato inserito
        response = ms.addLavoratore(validData);
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(1, ms.getLavoratori().size());

        // provo a ri-aggiungere lo stesso lavoratore: non dovrebbe permettermelo
        response = ms.addLavoratore(validData);
        assertEquals(ManagementSystemStatus.ACTION_FAILED, response.getStatus());
        assertEquals(1, ms.getLavoratori().size());
    }
}
