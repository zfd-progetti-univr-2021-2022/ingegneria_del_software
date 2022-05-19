/**
 * Esegue unit test sul management system.
 *
 * TODO: testare i metodi che si occupano di aggiungere, eliminare e modificare le esperienze lavorative dei lavoratori
 */
package com.example.progettoingegneria;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

import com.github.javafaker.Faker;

import static java.util.Objects.nonNull;
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

    /**
     * Dopo l'inizio e la fine di ogni test resetta i file JSON e il management system.
     * @throws IOException
     * @throws URISyntaxException
     */
    @BeforeEach
    @AfterEach
    void setUp() throws IOException, URISyntaxException {
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
            "    \"password\":\"secret\",",
            "    \"codiceFiscale\":\"ABCD\"",
            "    }",
            "]"
        );
        Files.write(dipendentiFile.toPath(), emptyJSON, StandardCharsets.UTF_8);
        Files.write(lavoratoriFile.toPath(), emptyJSON, StandardCharsets.UTF_8);
        Files.write(adminFile.toPath(), initAdminJSON, StandardCharsets.UTF_8);

        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);
        ms.logout();
        ManagementSystem.deleteInstance();
    }

    /** Numeri di telefono senza duplicati */
    private final Collection<String> telephoneNumbers = new ArrayList<>();
    /** Codici fiscali senza duplicati */
    private final Collection<String> codiciFiscali = new ArrayList<>();
    /** Password senza duplicati */
    private final Collection<String> passwords = new ArrayList<>();

    /**
     * Genera una password univoca che ha tra i 15 e i 20 caratteri e
     * che include lettere maiuscole, minuscole, caratteri speciali e numeri.
     * @return Password univoca
     */
    private String generateStrongPassword() {
        var password = new Faker().internet().password(15, 20, true, true, true);
        if (isPasswordValid(password) && (!passwords.contains(password))) {
            passwords.add(password);
            return password;
        }
        return generateStrongPassword();
    }

    /**
     * Si assicura che una password generata sia valida.
     *
     * La password deve avere:
     * - tra i 15 e i 20 caratteri
     * - almeno una lettera maiuscola e una minuscola
     * - almeno un carattere speciale tra $, &, @, #
     * - almeno un numero
     *
     * @param password Password da validare
     * @return true se la password e' valida, false altrimenti
     */
    private boolean isPasswordValid(String password) {
        return nonNull(password) && password.length() >= 15 && password.length() <= 20 &&
            password.chars().anyMatch(Character::isDigit) &&
            password.chars().anyMatch(Character::isLowerCase) &&
            password.chars().anyMatch(Character::isUpperCase) &&
            (password.chars().anyMatch(c -> c == '$') || password.chars().anyMatch(c -> c == '&') || password.chars().anyMatch(c -> c == '@') || password.chars().anyMatch(c -> c == '#'));
    }

    /**
     * Genera numero di telefono univoco che ha meno di 20 cifre.
     * @return Numero di telefono
     */
    private String generateTelephoneNumber(){
        String telephoneNumber = faker.phoneNumber().phoneNumber();

        while (telephoneNumber.length() > 20 || telephoneNumbers.contains(telephoneNumber)){
            telephoneNumber = faker.phoneNumber().phoneNumber();
        }
        telephoneNumbers.add(telephoneNumber);
        return telephoneNumber;
    }

    /**
     * Genera codice fiscale univoco
     * @return Codice fiscale
     */
    private String generateCodiceFiscale(){
        String codiceFiscale = faker.idNumber().ssnValid();
        while (codiciFiscali.contains(codiceFiscale)){
            codiceFiscale = faker.idNumber().ssnValid();
        }
        codiciFiscali.add(codiceFiscale);
        return codiceFiscale;
    }

    /**
     * Converte un tipo Date in LocalDate
     * @param date Data di tipo Date
     * @return data di tipo LocalDate
     */
    private LocalDate dateToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Si assicura che il sistema crei un solo management system
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    void testOneInstance() throws IOException, URISyntaxException {
        // se provo a creare due istanze mi aspetto di crearne una sola
        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);
        ManagementSystem ms2 = ManagementSystem.getInstance(resourcePath);
        assertEquals(ms, ms2);
    }

    /**
     * Effettua test di login e logout
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    void testLoginAndLogout() throws IOException, URISyntaxException {
        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);

        // all'inizio non dovrebbero esserci utenti autenticati
        assertNull(ms.getLoggedInUser());

        // provo a fare il logout senza entrare
        assertFalse(ms.logout());

        // provo a entrare inserendo valori nulli al posto di inserire username e password: dovrebbe dare errore
        assertThrows(IllegalArgumentException.class, () -> ms.login(null, null));

        // provo ad entrare con username e password errati: non dovrei riuscire ad accedere e quindi nessun utente e' autenticato
        assertFalse(ms.login("", ""));
        assertNull(ms.getLoggedInUser());

        // provo a entrare con password errata: non dovrei riuscire ad accedere
        assertFalse(ms.login("mario", ""));
        assertNull(ms.getLoggedInUser());

        // provo a inserire username e password di un utente esistente: dovrei essere in grado di entrare
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // provo a fare il logout: non dovrebbe essere piu' autenticato nessuno
        assertTrue(ms.logout());
        assertNull(ms.getLoggedInUser());
    }

    /**
     * Prova ad aggiungere e togliere lavoratori dal sistema
     * @throws IOException
     * @throws URISyntaxException
     */
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
            generateTelephoneNumber(),
            faker.address().streetAddress(),
            List.of(),
            List.of(),
            List.of(),
            false,
            List.of(),
            List.of(),
            generateCodiceFiscale()
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
            generateTelephoneNumber(),
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
                    generateTelephoneNumber(),
                    faker.internet().emailAddress()
                )
            ),
            generateCodiceFiscale()
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
        System.out.println(response.getDetails());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(1, ms.getLavoratori().size());

        // provo a ri-aggiungere lo stesso lavoratore: non dovrebbe permettermelo
        response = ms.addLavoratore(validData);
        assertEquals(ManagementSystemStatus.ACTION_FAILED, response.getStatus());
        assertEquals(1, ms.getLavoratori().size());

        // faccio il logout
        assertTrue(ms.logout());
        assertNull(ms.getLoggedInUser());

        // provo a rimuovere null
        assertThrows(IllegalArgumentException.class, () -> ms.removeLavoratore(null));
        assertEquals(1, ms.getLavoratori().size());

        // provo a rimuovere un lavoratore senza aver fatto il login
        response = ms.removeLavoratore(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.NOT_LOGGED_IN, response.getStatus());
        assertEquals(1, ms.getLavoratori().size());

        // rifaccio il login
        assertNull(ms.getLoggedInUser());
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // rimuovo un lavoratore che non esiste: dovrebbe fallire
        response = ms.removeLavoratore(generateCodiceFiscale());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        assertEquals(1, ms.getLavoratori().size());

        // rimuovo il lavoratore esistente: mi aspetto di rimuoverlo e che non ci siano piu' lavoratori nel sistema
        response = ms.removeLavoratore(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(0, ms.getLavoratori().size());

        // provo a rimuovere il lavoratore di nuovo: dovrebbe fallire
        response = ms.removeLavoratore(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        assertEquals(0, ms.getLavoratori().size());
    }

    /**
     * Prova ad aggiungere e rimuovere dipendenti
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    void testAddAndRemoveDipendenti() throws IOException, URISyntaxException {
        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);

        Dipendente invalidData = Dipendente.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "dipendente1",
            "pwdinsicura",
            generateCodiceFiscale()
        );

        // crea data attuale e sottraici 20 anni
        Date twentyYearsAgo = new Date();
        twentyYearsAgo.setYear(twentyYearsAgo.getYear()-20);

        Dipendente validData = Dipendente.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "dipendente2",
            generateStrongPassword(),
            generateCodiceFiscale()
        );

        Dipendente validData2 = Dipendente.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "dipendente3",
            generateStrongPassword(),
            generateCodiceFiscale()
        );

        // all'inizio non dovrebbe esserci nessun dipendente
        assertEquals(0, ms.getDipendenti().size());

        // aggiungo un dipendente nullo: lancia un errore, non dovrebbe essere inserito
        assertThrows(IllegalArgumentException.class, () -> ms.addDipendente(null));
        assertEquals(0, ms.getDipendenti().size());

        // aggiungo un dipendente senza accedere: non dovrebbe essere inserito
        ManagementSystemResponse response = ms.addDipendente(invalidData);
        assertEquals(ManagementSystemStatus.NOT_LOGGED_IN, response.getStatus());
        assertEquals(0, ms.getDipendenti().size());

        // effettuo il login
        assertNull(ms.getLoggedInUser());
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // aggiungo un dipendente con dati non validi: non dovrebbe essere inserito
        response = ms.addDipendente(invalidData);
        assertEquals(0, ms.getDipendenti().size());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        System.out.println(response.getDetails());

        // aggiungo un dipendente con dati validi: dovrebbe essere stato inserito
        response = ms.addDipendente(validData);
        System.out.println(response.getDetails());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());

        // provo a ri-aggiungere lo stesso dipendente: non dovrebbe permettermelo
        response = ms.addDipendente(validData);
        assertEquals(ManagementSystemStatus.ACTION_FAILED, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());

        // faccio il logout
        assertTrue(ms.logout());
        assertNull(ms.getLoggedInUser());

        // provo a rimuovere null
        assertThrows(IllegalArgumentException.class, () -> ms.removeDipendente(null));
        assertEquals(1, ms.getDipendenti().size());

        // provo a rimuovere un dipendente senza aver fatto il login
        response = ms.removeDipendente(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.NOT_LOGGED_IN, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());

        // rifaccio il login
        assertNull(ms.getLoggedInUser());
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // rimuovo un dipendente che non esiste: dovrebbe fallire
        response = ms.removeDipendente(generateCodiceFiscale());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());

        // rimuovo il dipendente esistente: mi aspetto di rimuoverlo e che non ci siano piu' dipendenti nel sistema
        response = ms.removeDipendente(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(0, ms.getDipendenti().size());

        // provo a rimuovere il dipendente di nuovo: dovrebbe fallire
        response = ms.removeDipendente(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        assertEquals(0, ms.getDipendenti().size());

        // inserisco di nuovo l'utente ed effettuo il login con il suo account
        response = ms.addDipendente(validData);
        System.out.println(response.getDetails());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());
        assertTrue(ms.logout());
        assertNull(ms.getLoggedInUser());
        assertTrue(ms.login(validData.getUsername(), validData.getPassword()));
        assertNotNull(ms.getLoggedInUser());

        // provo a creare un nuovo dipendente con l'account di un dipendente: non ho i permessi necessari
        response = ms.addDipendente(validData2);
        assertEquals(ManagementSystemStatus.PERMISSION_ERROR, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());

        // provo a rimuovere un dipendente con l'account di un dipendente: non ho i permessi necessari
        response = ms.removeDipendente(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.PERMISSION_ERROR, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());
    }

    /**
     * Prova ad aggiungere o rimuovere admin dal sistema
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    void testAddAndRemoveAdmin() throws IOException, URISyntaxException {
        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);

        Admin invalidData = Admin.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().future(10, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "admin1",
            "pwdinsicura",
            generateCodiceFiscale()
        );

        // crea data attuale e sottraici 20 anni
        Date twentyYearsAgo = new Date();
        twentyYearsAgo.setYear(twentyYearsAgo.getYear()-20);

        Admin validData = Admin.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "dipendente2",
            generateStrongPassword(),
            generateCodiceFiscale()
        );

        Admin validData2 = Admin.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "dipendente4",
            generateStrongPassword(),
            generateCodiceFiscale()
        );

        Dipendente validDataDipendente = Dipendente.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "dipendente3",
            generateStrongPassword(),
            generateCodiceFiscale()
        );

        // all'inizio dovrebbe esserci un solo admin
        assertEquals(1, ms.getAdmins().size());

        // aggiungo un admin nullo: lancia un errore, non dovrebbe essere inserito
        assertThrows(IllegalArgumentException.class, () -> ms.addAdmin(null));
        assertEquals(1, ms.getAdmins().size());

        // aggiungo un admin senza accedere: non dovrebbe essere inserito
        ManagementSystemResponse response = ms.addAdmin(validData);
        assertEquals(ManagementSystemStatus.NOT_LOGGED_IN, response.getStatus());
        assertEquals(1, ms.getAdmins().size());

        // aggiungo un dipendente senza accedere: non dovrebbe essere inserito
        response = ms.addDipendente(validDataDipendente);
        System.out.println(response);
        System.out.println(ms.getDipendenti());
        assertEquals(ManagementSystemStatus.NOT_LOGGED_IN, response.getStatus());
        assertEquals(0, ms.getDipendenti().size());

        // effettuo il login
        assertNull(ms.getLoggedInUser());
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // aggiungo un admin con dati non validi: non dovrebbe essere inserito
        response = ms.addAdmin(invalidData);
        System.out.println(response.getDetails());
        assertEquals(1, ms.getAdmins().size());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());

        // aggiungo un admin con dati validi: dovrebbe essere stato inserito
        response = ms.addAdmin(validData);
        System.out.println(response.getDetails());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(2, ms.getAdmins().size());

        // provo a ri-aggiungere lo stesso admin: non dovrebbe permettermelo
        response = ms.addAdmin(validData);
        System.out.println(response);
        assertEquals(ManagementSystemStatus.ACTION_FAILED, response.getStatus());
        assertEquals(2, ms.getAdmins().size());

        // faccio il logout
        assertTrue(ms.logout());
        assertNull(ms.getLoggedInUser());

        // provo a rimuovere null
        assertThrows(IllegalArgumentException.class, () -> ms.removeAdmin(null));
        assertEquals(2, ms.getAdmins().size());

        // provo a rimuovere un admin senza aver fatto il login
        response = ms.removeAdmin(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.NOT_LOGGED_IN, response.getStatus());
        assertEquals(2, ms.getAdmins().size());

        // rifaccio il login
        assertNull(ms.getLoggedInUser());
        assertTrue(ms.login("mario", "secret"));
        assertNotNull(ms.getLoggedInUser());

        // rimuovo un admin che non esiste: dovrebbe fallire
        response = ms.removeAdmin(generateCodiceFiscale());
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        assertEquals(2, ms.getAdmins().size());

        // provo a rimuovere me stesso: dovrebbe fallire
        String defaultAdminCodiceFiscale = ms.getLoggedInUser().getCodiceFiscale();
        response = ms.removeAdmin(defaultAdminCodiceFiscale);
        assertEquals(ManagementSystemStatus.REMOVE_SELF, response.getStatus());
        assertEquals(2, ms.getAdmins().size());

        // faccio il login con il nuovo admin ed elimino l'account admin iniziale
        ms.logout();
        ms.login(validData.getUsername(), validData.getPassword());
        response = ms.removeAdmin(defaultAdminCodiceFiscale);
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(1, ms.getAdmins().size());

        // provo di nuovo a eliminare me stesso: dovrebbe fallire
        response = ms.removeAdmin(ms.getLoggedInUser().getCodiceFiscale());
        assertEquals(ManagementSystemStatus.REMOVE_SELF, response.getStatus());
        assertEquals(1, ms.getAdmins().size());

        // provo a creare un nuovo dipendente con l'account di un admin: dovrebbe funzionare
        response = ms.addDipendente(validDataDipendente);
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(1, ms.getDipendenti().size());

        // esco dall'account admin ed entro nell'account dipendente
        ms.logout();
        ms.login(validDataDipendente.getUsername(), validDataDipendente.getPassword());

        // provo a rimuovere un admin con l'account di un dipendente: non ho i permessi richiesti
        response = ms.removeAdmin(validData.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.PERMISSION_ERROR, response.getStatus());
        assertEquals(1, ms.getAdmins().size());

        // provo ad aggiungere un admin con l'account di un dipendente: non ho i permessi richiesti
        response = ms.addAdmin(validData2);
        assertEquals(ManagementSystemStatus.PERMISSION_ERROR, response.getStatus());
        assertEquals(1, ms.getAdmins().size());

        // ri-effettuo il login con l'account admin
        ms.logout();
        ms.login(validData.getUsername(), validData.getPassword());

        // provo a rimuovere un dipendente con l'account di un admin: dovrebbe funzionare
        response = ms.removeDipendente(validDataDipendente.getCodiceFiscale());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        assertEquals(0, ms.getDipendenti().size());
    }

    /**
     * Aggiungi/rimuovi/modifica delle esperienze lavorative
     *
     * TODO: migliora i test e aggiungi i casi mancanti
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    void testAddAndRemoveEsperienzeLavorative() throws IOException, URISyntaxException {

        ManagementSystem ms = ManagementSystem.getInstance(resourcePath);

        // crea data attuale e sottraici 20 anni
        Date twentyYearsAgo = new Date();
        twentyYearsAgo.setYear(twentyYearsAgo.getYear()-20);

        Admin admin = Admin.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "admin",
            generateStrongPassword(),
            generateCodiceFiscale()
        );

        Dipendente dipendente = Dipendente.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
            "dipendente1",
            generateStrongPassword(),
            generateCodiceFiscale()
        );

        Lavoratore lavoratore = Lavoratore.of(
            faker.name().firstName(),
            faker.name().lastName(),
            faker.address().streetAddress(),
            faker.date().past(365*10, TimeUnit.DAYS, twentyYearsAgo).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
            faker.address().country(),
            faker.internet().emailAddress(),
            generateTelephoneNumber(),
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
                    generateTelephoneNumber(),
                    faker.internet().emailAddress()
                )
            ),
            generateCodiceFiscale()
        );

        // accedi con l'admin di default e crea un admin
        ms.login("mario", "secret");
        ms.addAdmin(admin);
        ms.logout();

        // accedi con l'account admin e crea un dipendente
        ms.login(admin.getUsername(), admin.getPassword());
        ms.addDipendente(dipendente);
        ms.logout();

        // accedi con l'account dipendente e crea lavoratore
        ms.login(dipendente.getUsername(), dipendente.getPassword());
        ms.addLavoratore(lavoratore);

        // verifica che le persone siano state inserite nel sistema
        assertEquals(2, ms.getAdmins().size());
        assertEquals(1, ms.getDipendenti().size());
        assertEquals(1, ms.getLavoratori().size());

        // prova ad aggiungere una esperienza lavorativa con dati non validi
        ManagementSystemResponse response = ms.addEsperienzaLavorativa(lavoratore.getCodiceFiscale(),
            EsperienzaLavorativa.of(
                dateToLocalDate(faker.date().between(new Date(2000, Calendar.JANUARY, 1), new Date(2000, Calendar.DECEMBER, 31))),
                dateToLocalDate(faker.date().between(new Date(2001, Calendar.JANUARY, 1), new Date(2001, Calendar.DECEMBER, 31))),
                "",
                List.of(""),
                "",
                200
            )
        );
        assertEquals(ManagementSystemStatus.INVALID_INPUT, response.getStatus());
        Lavoratore msLavoratore = (Lavoratore) ms.getLavoratori().toArray()[0];
        assertEquals(0, msLavoratore.getEsperienzeLavorative().size());

        // aggiungi una esperienza lavorativa
        response = ms.addEsperienzaLavorativa(lavoratore.getCodiceFiscale(),
            EsperienzaLavorativa.of(
                dateToLocalDate(faker.date().between(new Date(2000, Calendar.JANUARY, 1), new Date(2000, Calendar.DECEMBER, 31))),
                dateToLocalDate(faker.date().between(new Date(2001, Calendar.JANUARY, 1), new Date(2001, Calendar.DECEMBER, 31))),
                faker.company().name(),
                List.of(faker.job().title()),
                faker.address().streetAddress(),
                faker.number().numberBetween(50, 200)
            )
        );
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        msLavoratore = (Lavoratore) ms.getLavoratori().toArray()[0];
        EsperienzaLavorativa msLavoratoreEsperienzaLavorativa = (EsperienzaLavorativa) msLavoratore.getEsperienzeLavorative().toArray()[0];
        assertEquals(1, msLavoratore.getEsperienzeLavorative().size());

        // modifica l'esperienza lavorativa appena creata
        response = ms.modifyEsperienzaLavorativa(
            lavoratore.getCodiceFiscale(),
            msLavoratoreEsperienzaLavorativa.getId(),
            EsperienzaLavorativa.of(
                dateToLocalDate(faker.date().between(new Date(2000, Calendar.JANUARY, 1), new Date(2000, Calendar.DECEMBER, 31))),
                dateToLocalDate(faker.date().between(new Date(2001, Calendar.JANUARY, 1), new Date(2001, Calendar.DECEMBER, 31))),
                faker.company().name(),
                List.of(faker.job().title()),
                faker.address().streetAddress(),
                faker.number().numberBetween(50, 200)
            )
        );
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        msLavoratore = (Lavoratore) ms.getLavoratori().toArray()[0];
        msLavoratoreEsperienzaLavorativa = (EsperienzaLavorativa) msLavoratore.getEsperienzeLavorative().toArray()[0];
        assertEquals(1, msLavoratore.getEsperienzeLavorative().size());

        // rimuovi l'esperienza lavorativa
        ms.removeEsperienzaLavorativa(lavoratore.getCodiceFiscale(), msLavoratoreEsperienzaLavorativa.getId());
        assertEquals(ManagementSystemStatus.OK, response.getStatus());
        msLavoratore = (Lavoratore) ms.getLavoratori().toArray()[0];
        assertEquals(0, msLavoratore.getEsperienzeLavorative().size());
    }

    /**
     * Prova ad eseguire ricerche in and dei lavoratori.
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    @Test
    void searchLavoratoreAnd() throws IOException, URISyntaxException {
        ManagementSystem ms = ManagementSystem.getInstance(resourcePath + "/search_tests");
        ms.login("mario", "secret");

        // non specificare nessun filtro: mi aspetto di restituire tutti i lavoratori
        assertEquals(
            new TreeSet<>(ms.getLavoratori()),
            new TreeSet<>(ms.selectLavoratoriAnd(null, null, null, null, null, null, null, null))
        );

        // specifica il nome: dovrebbero esserci solo i lavoratori con questo nome
        List<Lavoratore> lavoratoriVincent = new ArrayList<>(
            ms.selectLavoratoriAnd("Vincent", null, null, null, null, null, null, null)
        );

        for (Lavoratore lavoratore: lavoratoriVincent){
            assertEquals("Vincent", lavoratore.getNome());
        }
    }
}
