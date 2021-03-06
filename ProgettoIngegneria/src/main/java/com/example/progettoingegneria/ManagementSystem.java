/**
 * Definisce la classe del management system.
 *
 * TODO: gestire account dipendenti e admin con lo stesso username. Due utenti non dovrebbero poter inserire lo stesso username
 *
 * TODO: impedire all'utente di eseguire operazioni su esperienze lavorative che hanno piu' di 5 anni
 */
package com.example.progettoingegneria;

import org.tinylog.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.*;

public class ManagementSystem {

    private static ManagementSystem instance = null;
    private Persona loggedInUser = null;
    private final Collection<Dipendente> dipendenti = new ArrayList<>();
    private final Collection<Lavoratore> lavoratori = new ArrayList<>();
    private final Collection<Admin> admins = new ArrayList<>();
    private final ObjectMapper objectMapper = JsonMapper.builder().enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER).build();
    private String resourcePath = new File(ManagementSystem.class.getResource("dipendenti.json").getFile()).toPath().getParent().toString();
    private File dipendentiFile = Paths.get(resourcePath, "dipendenti.json").toFile();
    private File lavoratoriFile = Paths.get(resourcePath, "lavoratori.json").toFile();
    private File adminFile = Paths.get(resourcePath, "admin.json").toFile();

    /**
     * Costruttore del Management System:
     * imposta il mapper della libreria jackson e importa i file JSON
     * @throws IOException
     */
    private ManagementSystem(String customPath) throws IOException {
        if (customPath != null){
            this.resourcePath = customPath;
            this.dipendentiFile = Paths.get(resourcePath, "dipendenti.json").toFile();
            this.lavoratoriFile = Paths.get(resourcePath, "lavoratori.json").toFile();
            this.adminFile = Paths.get(resourcePath, "admin.json").toFile();
        }
        objectMapper.findAndRegisterModules();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        Logger.info("Inizio setup iniziale");
        this.initialSetup();
        Logger.debug("Fine setup iniziale");
    }

    /**
     * Se non esistono altre istanze del Management System la crea e la restituisce.
     * Altrimenti restituisce l'istanza gia' esistente.
     *
     * Usa il percorso di default dei file JSON.
     *
     * @return Istanza del Management System
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     * @throws URISyntaxException
     */
    public static ManagementSystem getInstance() throws IOException, URISyntaxException {
        return getInstance(null);
    }

    /**
     * Se non esistono altre istanze del Management System la crea e la restituisce.
     * Altrimenti restituisce l'istanza gia' esistente.
     *
     * Usa il percorso personalizzato per trovare i file JSON.
     *
     * @return Istanza del Management System
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     * @throws URISyntaxException
     */
    public static ManagementSystem getInstance(String customPath) throws IOException, URISyntaxException {
        if (ManagementSystem.instance == null){
            Logger.debug("Creata istanza del management system con percorso file JSON custom");
            ManagementSystem.instance = new ManagementSystem(customPath);
        }
        return ManagementSystem.instance;
    }

    /**
     * Elimina l'istanza del management system: usato per fare il reset del sistema.
     */
    protected static void deleteInstance(){
        ManagementSystem.instance = null;
    }

    /**
     * Gestisce il setup iniziale del Management System: importa i file JSON.
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    private void initialSetup() throws IOException {
        Logger.debug("Importo file JSON");
        this.importJSONDipendenti(dipendentiFile);
        this.importJSONLavoratori(lavoratoriFile);
        this.importJSONAdmin(adminFile);
    }

    /**
     * Importa i dipendenti dal file JSON filePath.
     * @param filePath File JSON con dipendenti in input
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    private void importJSONDipendenti(File filePath) throws IOException {
        Logger.debug("Importo dipendenti dal file JSON {}", filePath.toString());
        List<Dipendente> dipendenti = Arrays.asList(objectMapper.readValue(filePath, Dipendente[].class));
        this.dipendenti.addAll(dipendenti);
    }

    /**
     * Importa i lavoratori dal file JSON filePath.
     * @param filePath File JSON con lavoratori in input
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    private void importJSONLavoratori(File filePath) throws IOException {
        Logger.debug("Importo lavoratori dal file JSON {}", filePath.toString());
        List<Lavoratore> lavoratori = Arrays.asList(objectMapper.readValue(filePath, Lavoratore[].class));
        this.lavoratori.addAll(lavoratori);
    }

    /**
     * Importa gli admin dal file JSON filePath.
     * @param filePath File JSON con admin in input
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    private void importJSONAdmin(File filePath) throws IOException {
        Logger.debug("Importo admin dal file JSON {}", filePath.toString());
        List<Admin> admins = Arrays.asList(objectMapper.readValue(filePath, Admin[].class));
        this.admins.addAll(admins);
    }

    /**
     * Esporta i dipendenti come file JSON del file filePath.
     * @param filePath File JSON con dipendenti in output
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    private void exportJSONDipendenti(File filePath) throws IOException {
        Logger.debug("Esporto dipendenti su file JSON {}", filePath.toString());
        PrintWriter writer = new PrintWriter(filePath);
        objectMapper.writeValue(writer, this.dipendenti);
        writer.close();
    }

    /**
     * Esporta i lavoratori come file JSON del file filePath.
     * @param filePath File JSON con lavoratori in output
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    private void exportJSONLavoratori(File filePath) throws IOException {
        Logger.debug("Esporto lavoratori su file JSON {}", filePath.toString());
        PrintWriter writer = new PrintWriter(filePath);
        objectMapper.writeValue(writer, this.lavoratori);
        writer.close();
    }

    /**
     * Esporta gli admin come file JSON del file filePath.
     * @param filePath File JSON con admin in output
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    private void exportJSONAdmins(File filePath) throws IOException {
        Logger.debug("Esporto admin su file JSON {}", filePath.toString());
        PrintWriter writer = new PrintWriter(filePath);
        objectMapper.writeValue(writer, this.admins);
        writer.close();
    }

    /**
     * Memorizza su file JSON le modifiche effettuate precedentemente.
     * @throws IOException Errore che puo' avvenire durante la lettura dei file JSON
     */
    public void commitChanges() throws IOException {
        Logger.info("Memorizzo modifiche sui file JSON");
        exportJSONDipendenti(dipendentiFile);
        exportJSONLavoratori(lavoratoriFile);
        exportJSONAdmins(adminFile);
    }

    /**
     * Gestisce il login dell'utente.
     * @param username Username dell'utente
     * @param password Password dell'utente
     * @return true se il login ha avuto successo, false altrimenti
     */
    public boolean login(String username, String password){
        if (username == null || password == null){
            throw new IllegalArgumentException("username e password non possono essere nulli");
        }

        Logger.info("L'utente {} vuole autenticarsi", username);

        if (loggedInUser == null){
            for (Admin admin: admins) {
                if (admin.getUsername().equals(username) && admin.getPassword().equals(password)){
                    loggedInUser = admin;
                    Logger.info("Utente {} autenticato come admin", username);
                    return true;
                }
            }

            for (Dipendente dipendente: dipendenti) {
                if (dipendente.getUsername().equals(username) && dipendente.getPassword().equals(password)){
                    loggedInUser = dipendente;
                    Logger.info("Utente {} autenticato come dipendente", username);
                    return true;
                }
            }
        }

        Logger.info("Non e' stato possibile autenticare l'utente {}: username/password errata", username);
        return false;
    }

    /**
     * Restituisce l'utente attualmente autenticato nel Management System.
     * @return utente attualmente autenticato nel Management System
     */
    public Persona getLoggedInUser(){
        return loggedInUser;
    }

    /**
     * Gestisce il logout dell'utente.
     * @return true se l'utente e' stato disconnesso, false se l'utente non era autenticato
     */
    public boolean logout(){
        if (loggedInUser == null){
            return false;
        }
        Logger.info("L'utente {} vuole effettuare il logout", ((Dipendente)loggedInUser).getUsername());
        loggedInUser = null;
        return true;
    }

    /**
     * Restituisce i lavoratori nel sistema.
     * @return lavoratori nel sistema
     */
    public Collection<Lavoratore> getLavoratori() {
        return lavoratori;
    }

    /**
     * Restituisce i dipendenti nel sistema.
     * @return dipendenti nel sistema
     */
    public Collection<Dipendente> getDipendenti() {
        return dipendenti;
    }

    /**
     * Restituisce gli admin nel sistema.
     * @return Admin nel sistema
     */
    public Collection<Admin> getAdmins() {
        return admins;
    }

    /**
     * Gestisce la creazione di un account lavoratore.
     *
     * Un utente puo' creare un account lavoratore se:
     * - ha effettuato l'accesso
     * - e' dipendente (o admin)
     * - i dati del lavoratore da inserire sono validi
     *
     * @param lavoratore Lavoratore da aggiungere dal sistema
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore che puo' avvenire durante il salvataggio delle modifiche su file
     * @throws URISyntaxException
     */
    public ManagementSystemResponse addLavoratore(Lavoratore lavoratore) throws IOException, URISyntaxException {
        if (lavoratore == null)
            throw new IllegalArgumentException("lavoratore non puo' essere null");

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        Logger.info("Un utente vuole iscrivere il lavoratore {} {}", lavoratore.getNome(), lavoratore.getCognome());

        if (!loggedInUser.isDipendente()) {
            Logger.info("L'utente non ha i permessi necessari per iscrivere il lavoratore");
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo i dipendenti e gli admin possono aggiungere lavoratori")
            );
        }

        Dipendente utente = (Dipendente) loggedInUser;

        if (lavoratore.validate().size() > 0 ) {
            return new ManagementSystemResponse(
                ManagementSystemStatus.INVALID_INPUT,
                lavoratore.validate()
            );
        }

        if (!((!lavoratori.contains(lavoratore)) && lavoratori.add(lavoratore))) {
            return new ManagementSystemResponse(
                ManagementSystemStatus.ACTION_FAILED,
                List.of("Non e' stato possibile eseguire l'azione: possibile duplicato")
            );
        }

        commitChanges();
        Logger.info(
            "L'utente {} ha aggiunto il lavoratore {} {}",
            utente.getUsername(), lavoratore.getNome(), lavoratore.getCognome()
        );
        return new ManagementSystemResponse(
            ManagementSystemStatus.OK,
            List.of()
        );
    }

    /**
     * Gestisce la eliminazione di un lavoratore.
     *
     * Un utente puo' eliminare un lavoratore se:
     * - ha effettuato l'accesso
     * - e' dipendente (o admin)
     *
     * @param codiceFiscale Codice fiscale del lavoratore da rimuovere dal sistema
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore che puo' avvenire durante il salvataggio delle modifiche su file
     */
    public ManagementSystemResponse removeLavoratore(String codiceFiscale) throws IOException {

        if (codiceFiscale == null)
            throw new IllegalArgumentException("codiceFiscale non puo' essere null");

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        Logger.info("Un utente vuole rimuovere il lavoratore con codice fiscale {}", codiceFiscale);

        if (!loggedInUser.isDipendente())
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo i dipendenti e gli admin possono rimuovere lavoratori")
            );

        Dipendente utente = (Dipendente) loggedInUser;

        Lavoratore lavoratoreToRemove = null;
        for (Lavoratore lavoratore: this.lavoratori) {
            if (lavoratore.getCodiceFiscale().equals(codiceFiscale)){
                lavoratoreToRemove = lavoratore;
                break;
            }
        }

        if (lavoratoreToRemove == null) {
            // lavoratore non trovato
            return new ManagementSystemResponse(
                ManagementSystemStatus.INVALID_INPUT,
                List.of("Non e' stato possibile trovare un lavoratore con il codice fiscale specificato")
            );
        }

        Logger.info("L'utente {} vuole rimuovere il lavoratore {} {}", utente.getUsername(), lavoratoreToRemove.getNome(), lavoratoreToRemove.getCognome());

        if (!(this.lavoratori.contains(lavoratoreToRemove) && lavoratori.remove(lavoratoreToRemove))) {
            Logger.info(
                "Non e' stato possibile rimuovere il lavoratore {} {} da parte dell'utente {}",
                lavoratoreToRemove.getNome(), lavoratoreToRemove.getCognome(), utente.getUsername()
            );

            return new ManagementSystemResponse(
                ManagementSystemStatus.ACTION_FAILED,
                List.of("Non e' stato possibile eseguire l'azione")
            );
        }

        commitChanges();
        Logger.info(
            "L' utente {} ha rimosso il lavoratore {} {}",
            utente.getUsername(), lavoratoreToRemove.getNome(), lavoratoreToRemove.getCognome()
        );
        return new ManagementSystemResponse(
            ManagementSystemStatus.OK,
            List.of()
        );
    }

    /**
     * Modifica un lavoratore esistente nel sistema.
     *
     * @param lavoratore Lavoratore con attributi modificati
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore che puo' avvenire durante il salvataggio delle modifiche su file
     * @throws URISyntaxException
     */
    public ManagementSystemResponse modifyLavoratore(Lavoratore lavoratore) throws IOException, URISyntaxException {
        // ottieni le vecchie informazioni per permettere il ripristino dei dati in caso di fallimento
        Lavoratore oldLavoratore = this.getLavoratore(lavoratore.getCodiceFiscale());
        if (oldLavoratore == null){
            return new ManagementSystemResponse(
                ManagementSystemStatus.ACTION_FAILED,
                List.of("Non e' stato possibile trovare il lavoratore per cui effettuare le modifiche")
            );
        }

        // tenta di eliminare il lavoratore con le vecchie informazioni
        ManagementSystemResponse response = this.removeLavoratore(lavoratore.getCodiceFiscale());
        if (response.getStatus() != ManagementSystemStatus.OK){
            return response;
        }

        // inserisci il lavoratore con le nuove informazioni
        response = this.addLavoratore(lavoratore);

        if (response.getStatus() != ManagementSystemStatus.OK){
            // non ha funzionato: reinserisci le vecchie informazioni
            this.addLavoratore(oldLavoratore);
            return response;
        }

        return new ManagementSystemResponse(ManagementSystemStatus.OK, List.of());
    }

    /**
     * Restituisce il lavoratore avente un certo codice fiscale.
     * Restituisce null se non e' possibile trovare il lavoratore (non e' presente nel sistema o non si hanno i permessi sufficienti)
     *
     * @param codiceFiscale codice fiscale del lavoratore da selezionare
     * @return Lavoratore con il codice fiscale passato come parametro
     */
    private Lavoratore getLavoratore(String codiceFiscale){
        if (codiceFiscale == null)
            throw new IllegalArgumentException("codiceFiscale non puo' essere nullo");

        if (loggedInUser == null)
            return null;

        Logger.info("Un utente vuole recuperare il lavoratore con codice fiscale {}", codiceFiscale);

        if (!loggedInUser.isDipendente()) {
            Logger.info("L'utente non ha i permessi necessari per ottenere informazioni sul lavoratore");
            return null;
        }

        for (Lavoratore lavoratore: lavoratori){
            if (lavoratore.getCodiceFiscale().equals(codiceFiscale)){
                return lavoratore;
            }
        }
        return null;
    }

    /**
     * Gestisce la creazione di un account dipendente.
     *
     * Un utente puo' creare un account dipendente se:
     * - ha effettuato l'accesso
     * - e' admin
     * - i dati del dipendente da inserire sono validi (es. requisiti di password)
     *
     * @param dipendente Dipendente da aggiungere dal sistema
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore che puo' avvenire durante il salvataggio delle modifiche su file
     * @throws URISyntaxException
     */
    public ManagementSystemResponse addDipendente(Dipendente dipendente) throws IOException, URISyntaxException {
        if (dipendente == null){
            throw new IllegalArgumentException("dipendente non puo' essere nullo");
        }

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        Logger.info("Un utente vuole aggiungere il dipendente {} {}", dipendente.getNome(), dipendente.getCognome());

        if (!loggedInUser.isAdmin())
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo gli admin possono aggiungere altri dipendenti")
            );

        Dipendente utente = (Dipendente) loggedInUser;

        if (dipendente.validate().size() > 0) {
            return new ManagementSystemResponse(
                ManagementSystemStatus.INVALID_INPUT,
                dipendente.validate()
            );
        }

        if (dipendenti.contains(dipendente) || (!dipendenti.add(dipendente))) {
            return new ManagementSystemResponse(
                ManagementSystemStatus.ACTION_FAILED,
                List.of("Non e' stato possibile eseguire l'azione")
            );
        }

        commitChanges();
        Logger.info("L'utente {} ha inserito il dipendente {} {} (username {})", utente.getUsername(), dipendente.getNome(), dipendente.getCognome(), dipendente.getUsername());
        return new ManagementSystemResponse(
            ManagementSystemStatus.OK,
            List.of()
        );
    }

    /**
     * Gestisce la eliminazione di un account dipendente.
     *
     * Un utente puo' eliminare un account dipendente se:
     * - ha effettuato l'accesso
     * - e' admin
     *
     * @param codiceFiscale Codice fiscale dipendente da rimuovere dal sistema
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore che puo' apparire durante salvataggio modifiche su file
     */
    public ManagementSystemResponse removeDipendente(String codiceFiscale) throws IOException {
        if (codiceFiscale == null)
            throw new IllegalArgumentException("codiceFiscale non puo' essere nullo");

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        if (!loggedInUser.isAdmin())
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo gli admin possono aggiungere altri dipendenti")
            );

        Dipendente dipendenteToRemove = null;
        for (Dipendente dipendente: this.dipendenti) {
            if (dipendente.getCodiceFiscale().equals(codiceFiscale)){
                dipendenteToRemove = dipendente;
                break;
            }
        }

        if (dipendenteToRemove == null) {
            // dipendente non trovato
            return new ManagementSystemResponse(
                ManagementSystemStatus.INVALID_INPUT,
                List.of("Non e' stato possibile trovare un dipendente con il codice fiscale specificato")
            );
        }

        if (dipendenti.remove(dipendenteToRemove)) {
            commitChanges();
            return new ManagementSystemResponse(
                ManagementSystemStatus.OK,
                List.of()
            );
        }

        return new ManagementSystemResponse(
            ManagementSystemStatus.ACTION_FAILED,
            List.of("Non e' stato possibile eseguire l'azione")
        );
    }

    /**
     * Gestisce la creazione di un account admin.
     *
     * L'utente puo' registrare un account admin se:
     * - ha effettuato l'accesso
     * - se e' anch'esso un admin
     * - se i dati dell'admin nuovo rispettano tutti i requisiti (es. password abbastanza lunga, con caratteri speciali, ...)
     *
     * @param admin Admin da aggiungere al sistema
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore che puo' apparire durante salvataggio modifiche su file
     */
    public ManagementSystemResponse addAdmin(Admin admin) throws IOException {
        if (admin == null){
            throw new IllegalArgumentException("admin non puo' essere nullo");
        }

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        if (!loggedInUser.isAdmin())
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo gli admin possono aggiungere altri admin")
            );

        if (admin.validate().size() > 0 ) {
            return new ManagementSystemResponse(
                ManagementSystemStatus.INVALID_INPUT,
                admin.validate()
            );
        }

        if (admins.contains(admin) || (!admins.add(admin))){
            return new ManagementSystemResponse(
                ManagementSystemStatus.ACTION_FAILED,
                List.of("Non e' stato possibile eseguire l'azione")
            );
        }

        commitChanges();
        return new ManagementSystemResponse(
            ManagementSystemStatus.OK,
            List.of()
        );
    }

    /**
     * Gestisce l'eliminazione di un account admin da parte di un utente.
     *
     * Un utente puo' eliminare un admin se:
     * - ha effettuato l'accesso
     * - e' anch'esso admin
     * - non sta eliminando se stesso
     * - non e' l'unico admin presente nel sistema
     *
     * @param codiceFiscale Codice fiscale admin da rimuovere dal sistema
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore durante salvataggio modifiche
     */
    public ManagementSystemResponse removeAdmin(String codiceFiscale) throws IOException {
        if (codiceFiscale == null)
            throw new IllegalArgumentException("codiceFiscale non puo' essere nullo");

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        if (!loggedInUser.isAdmin())
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo gli admin possono rimuovere altri admin")
            );

        Admin adminToRemove = null;
        for (Admin admin: this.admins) {
            if (admin.getCodiceFiscale().equals(codiceFiscale)){
                adminToRemove = admin;
                break;
            }
        }

        if (adminToRemove == null) {
            // admin non trovato
            return new ManagementSystemResponse(
                ManagementSystemStatus.INVALID_INPUT,
                List.of("Non e' stato possibile trovare un admin con il codice fiscale specificato")
            );
        }

        if (adminToRemove.equals(loggedInUser)){
            return new ManagementSystemResponse(
                ManagementSystemStatus.REMOVE_SELF,
                List.of("Un admin non puo' auto-disiscriversi dal sistema")
            );
        }

        if (admins.size() <= 1)
            return new ManagementSystemResponse(
                ManagementSystemStatus.ONE_ADMIN,
                List.of("Impossibile rimuovere l'admin: e' l'unico admin registrato nel sistema")
            );

        if (!admins.remove(adminToRemove)) {
            return new ManagementSystemResponse(
                ManagementSystemStatus.ACTION_FAILED,
                List.of("Non e' stato possibile eseguire l'azione")
            );
        }

        commitChanges();
        return new ManagementSystemResponse(
            ManagementSystemStatus.OK,
            List.of()
        );
    }

    /**
     * Aggiunge un'esperienza lavorativa ad un lavoratore
     * @param codiceFiscale Codice fiscale del lavoratore
     * @param esperienzaLavorativa Esperienza lavorativa da aggiungere
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore durante salvataggio modifiche
     */
    public ManagementSystemResponse addEsperienzaLavorativa(String codiceFiscale, EsperienzaLavorativa esperienzaLavorativa) throws IOException {
        if (codiceFiscale == null || esperienzaLavorativa == null){
            throw new IllegalArgumentException("codiceFiscale e esperienzaLavorativa non possono essere nulli");
        }

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        Logger.info("Un utente vuole aggiungere un'esperienza lavorativa al lavoratore con codice fiscale {}", codiceFiscale);

        if (!loggedInUser.isDipendente()) {
            Logger.info("L'utente non ha i permessi necessari per aggiungere l'esperienza lavorativa");
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo i dipendenti e gli admin possono aggiungere esperienze lavorative")
            );
        }

        if (esperienzaLavorativa.validate().size() > 0 ) {
            return new ManagementSystemResponse(
                ManagementSystemStatus.INVALID_INPUT,
                esperienzaLavorativa.validate()
            );
        }

        Dipendente utente = (Dipendente) loggedInUser;

        for (Lavoratore lavoratore: this.lavoratori) {
            if (!lavoratore.getCodiceFiscale().equals(codiceFiscale)){
                continue;
            }

            // trovato lavoratore
            if (!lavoratore.addEsperienzaLavorativa(esperienzaLavorativa)) {
                return new ManagementSystemResponse(
                    ManagementSystemStatus.ACTION_FAILED,
                    List.of("Non e' stato possibile eseguire l'azione: possibile duplicato")
                );
            }

            commitChanges();
            Logger.info(
                "L'utente {} ha aggiunto una nuova esperienza lavorativa al lavoratore {} {}",
                utente.getUsername(), lavoratore.getNome(), lavoratore.getCognome()
            );
            return new ManagementSystemResponse(
                ManagementSystemStatus.OK,
                List.of()
            );
        }

        // lavoratore non trovato
        return new ManagementSystemResponse(
            ManagementSystemStatus.INVALID_INPUT,
            List.of("Non e' stato possibile trovare un lavoratore con il codice fiscale specificato")
        );
    }

    /**
     * Rimuove un'esperienza lavorativa ad un lavoratore
     * @param codiceFiscale Codice fiscale del lavoratore
     * @param esperienzaLavorativaId Identificativo esperienza lavorativa da rimuovere
     * @return ManagementSystemResponse Risposta del sistema con status e eventuali messaggi di errore
     * @throws IOException Errore durante salvataggio modifiche
     */
    public ManagementSystemResponse removeEsperienzaLavorativa(String codiceFiscale, int esperienzaLavorativaId) throws IOException {
        if (codiceFiscale == null){
            throw new IllegalArgumentException("codiceFiscale non puo' essere nullo");
        }

        if (loggedInUser == null)
            return new ManagementSystemResponse(
                ManagementSystemStatus.NOT_LOGGED_IN,
                List.of("Per eseguire questa operazione e' necessario effettuare l'accesso")
            );

        Logger.info("Un utente vuole rimuovere un'esperienza lavorativa al lavoratore con codice fiscale {}", codiceFiscale);

        if (!loggedInUser.isDipendente()) {
            Logger.info("L'utente non ha i permessi necessari per rimuovere esperienze lavorative");
            return new ManagementSystemResponse(
                ManagementSystemStatus.PERMISSION_ERROR,
                List.of("Solo i dipendenti e gli admin possono rimuovere esperienze lavorative")
            );
        }

        Dipendente utente = (Dipendente) loggedInUser;

        for (Lavoratore lavoratore: this.lavoratori) {
            if (!lavoratore.getCodiceFiscale().equals(codiceFiscale)){
                continue;
            }

            // trovato lavoratore
            if (!lavoratore.removeEsperienzaLavorativa(esperienzaLavorativaId)) {
                return new ManagementSystemResponse(
                    ManagementSystemStatus.ACTION_FAILED,
                    List.of("Non e' stato possibile eseguire l'azione")
                );
            }

            commitChanges();
            Logger.info(
                "L'utente {} ha rimosso un'attivita' lavorativa del lavoratore {} {}",
                utente.getUsername(), lavoratore.getNome(), lavoratore.getCognome()
            );
            return new ManagementSystemResponse(
                ManagementSystemStatus.OK,
                List.of()
            );
        }

        // lavoratore non trovato
        return new ManagementSystemResponse(
            ManagementSystemStatus.INVALID_INPUT,
            List.of("Non e' stato possibile trovare un lavoratore con il codice fiscale specificato")
        );
    }

    /**
     * Modifica una esperienza lavorativa all'interno del sistema
     * @param codiceFiscale Codice fiscale del lavoratore
     * @param esperienzaLavorativaId Identificativo dell'esperienza lavorativa
     * @param newEsperienzaLavorativa Nuova esperienza lavorativa che sostituisce l'esperienza lavorativa attuale
     * @return ManagementSystemResponse Risposta del sistema con status ed eventuali messaggi di errore
     * @throws IOException Errore durante salvataggio modifiche
     */
    public ManagementSystemResponse modifyEsperienzaLavorativa(String codiceFiscale, int esperienzaLavorativaId, EsperienzaLavorativa newEsperienzaLavorativa) throws IOException {
        EsperienzaLavorativa originalEsperienzaLavorativa = getEsperienzaLavorativa(codiceFiscale, esperienzaLavorativaId);
        if (originalEsperienzaLavorativa == null){
            return new ManagementSystemResponse(
                ManagementSystemStatus.ACTION_FAILED,
                List.of("Non e' stato possibile trovare l'esperienza lavorativa e/o il lavoratore per cui effettuare le modifiche")
            );
        }

        // imposta id della vecchia esperienza lavorativa e prova a cancellarla tra le esperienze lavorative dell'utente
        newEsperienzaLavorativa.setId(originalEsperienzaLavorativa.getId());
        removeEsperienzaLavorativa(codiceFiscale, esperienzaLavorativaId);

        // prova ad aggiungere l'esperienza lavorativa modificata
        ManagementSystemResponse response = addEsperienzaLavorativa(codiceFiscale, newEsperienzaLavorativa);

        // se la richiesta non ha avuto successo elimina l'esperienza lavorativa modificata e riaggiungi l'originale
        if (response.getStatus() != ManagementSystemStatus.OK){
            removeEsperienzaLavorativa(codiceFiscale, newEsperienzaLavorativa.getId());
            addEsperienzaLavorativa(codiceFiscale, originalEsperienzaLavorativa);
            return response;
        }

        return new ManagementSystemResponse(ManagementSystemStatus.OK, List.of());
    }

    /**
     * Recupera una esperienza lavorativa da un lavoratore.
     * Restituisce null se non e' stato possibile trovare lavoratore/esperienza lavorativa.
     *
     * @param codiceFiscale Codice fiscale del lavoratore
     * @param esperienzaLavorativaId Identificativo esperienza lavorativa
     * @return Esperienza lavorativa selezionata oppure null in caso di errore
     */
    private EsperienzaLavorativa getEsperienzaLavorativa(String codiceFiscale, int esperienzaLavorativaId) {
        if (codiceFiscale == null){
            throw new IllegalArgumentException("codiceFiscale non puo' essere nullo");
        }

        if (loggedInUser == null)
            return null;

        Logger.info("Un utente vuole recuperare l'esperienza lavorativa con id {} del lavoratore con codice fiscale {}", esperienzaLavorativaId, codiceFiscale);

        if (!loggedInUser.isDipendente()) {
            Logger.info("L'utente non ha i permessi necessari per ottenere informazioni sull'esperienza lavorativa");
            return null;
        }

        for (Lavoratore lavoratore: this.lavoratori) {
            if (!lavoratore.getCodiceFiscale().equals(codiceFiscale)){
                continue;
            }

            // trovato lavoratore
            for (EsperienzaLavorativa esperienzaLavorativa: lavoratore.getEsperienzeLavorative()){
                if (esperienzaLavorativa.getId() == esperienzaLavorativaId){
                    return esperienzaLavorativa;
                }
            }
            return null;
        }

        // lavoratore non trovato
        return null;
    }

    /**
     * Restituisce tutti i lavoratori che hanno le caratteristiche passate come parametro.
     *
     * Se un parametro e' null quell'attributo verra' ignorato.
     *
     * TODO: selezionare solo le patenti richieste (?) da chi ricerca
     *
     * @param nome Nome dei lavoratori da cercare
     * @param cognome Cognome dei lavoratori da cercare
     * @param lingueParlate Lingue parlate dai lavoratori da cercare
     * @param periodiDisponibilita Periodi di disponibilita' dei lavoratori da cercare
     * @param mansioni Mansioni svolte dai lavoratori da cercare
     * @param luogoResidenza Luogo di residenza dei lavoratori da cercare
     * @param automunito true se i lavoratori da cercare sono automuniti, false se non sono automuniti
     * @param patenti Patenti dei lavoratori da cercare
     * @return Insieme di lavoratori che hanno tutte le caratteristiche specificate
     */
    public Collection<Lavoratore> selectLavoratoriAnd(String nome, String cognome, Collection<Lingua> lingueParlate, Collection<PeriodoDisponibilita> periodiDisponibilita, Collection<String> mansioni, String luogoResidenza, Boolean automunito, Collection<Patente> patenti){

        HashSet<Lavoratore> lavoratoriSelezionati = new HashSet<>();

        if (loggedInUser == null)
            return lavoratoriSelezionati;

        if (!loggedInUser.isDipendente())
            return lavoratoriSelezionati;

        for (Lavoratore lavoratore: this.lavoratori){

            if (!checkEqualIfNotNull(nome, lavoratore.getNome()))
                continue;

            if (!checkEqualIfNotNull(cognome, lavoratore.getCognome()))
                continue;

            // controlla lingue parlate
            if (!checkIfElementsInLingueCollection(lingueParlate, lavoratore.getLingueParlate()))
                continue;

            // controlla periodi disponibilita'
            if (!checkIfElementsInPeriodiDisponibilitaCollection(periodiDisponibilita, lavoratore.getPeriodiDisponibilita()))
                continue;

            // controlla mansioni
            if (!checkIfElementsInMansioniCollection(mansioni, lavoratore.getMansioni()))
                continue;

            if (!checkEqualIfNotNull(luogoResidenza, lavoratore.getIndirizzoResidenza()))
                continue;

            if (!checkEqualIfNotNull(automunito, lavoratore.getAutomunito()))
                continue;

            // controlla patenti
            if (patenti != null){
                if (patenti.size() == 0)
                    continue;
            }

            lavoratoriSelezionati.add(lavoratore);
        }

        return lavoratoriSelezionati;
    }

    /**
     * Restituisce true se "a" e "b" sono equals oppure se uno dei due e' null.
     * @param a Valore String
     * @param b Valore String
     * @return true se uno dei due parametri e' null oppure se sono entrambi equals
     */
    private boolean checkEqualIfNotNull(String a, String b){
        if (a == null || b == null){
            return true;
        }
        return a.equals(b);
    }

    /**
     * Restituisce true se "a" e "b" sono equals oppure se uno dei due e' null.
     * @param a Valore Boolean
     * @param b Valore Boolean
     * @return true se uno dei due parametri e' null oppure se sono entrambi equals
     */
    private boolean checkEqualIfNotNull(Boolean a, Boolean b){
        if (a == null || b == null){
            return true;
        }
        return a.equals(b);
    }

    /**
     * Restituisce true se "elements" sono contenuti in "collection" oppure se uno dei due e' null.
     * @param elements Collezione di lingue
     * @param collection Collezione di lingue che dovrebbero contenere "elements"
     * @return true se "elements" sono contenuti in "collection" oppure se uno dei due e' null
     */
    private boolean checkIfElementsInLingueCollection(Collection<Lingua> elements, Collection<Lingua> collection){
        if (elements == null || collection == null){
            return true;
        }

        return collection.containsAll(elements);
    }

    /**
     * Restituisce true se "elements" sono contenuti in "collection" oppure se uno dei due e' null.
     * @param elements Collezione di periodi di disponibilita'
     * @param collection Collezione di periodi di disponibilita' che dovrebbero contenere "elements"
     * @return true se "elements" sono contenuti in "collection" oppure se uno dei due e' null
     */
    private boolean checkIfElementsInPeriodiDisponibilitaCollection(Collection<PeriodoDisponibilita> elements, Collection<PeriodoDisponibilita> collection){
        if (elements == null || collection == null){
            return true;
        }

        return collection.containsAll(elements);
    }

    /**
     * Restituisce true se "elements" sono contenuti in "collection" oppure se uno dei due e' null.
     * @param elements Collezione di stringhe
     * @param collection Collezione di stringhe che dovrebbero contenere "elements"
     * @return true se "elements" sono contenuti in "collection" oppure se uno dei due e' null
     */
    private boolean checkIfElementsInMansioniCollection(Collection<String> elements, Collection<String> collection){
        if (elements == null || collection == null){
            return true;
        }

        return collection.containsAll(elements);
    }

    /**
     * Restituisce una stringa con dipendenti, lavoratori e admin nel sistema
     * @return Stringa con dipendenti, lavoratori e admin
     */
    @Override
    public String toString() {
        return "ManagementSystem{" +
            "Dipendenti" + dipendenti.toString() +
            "Lavoratori" + lavoratori.toString() +
            "Admin" + admins.toString() +
            "}";
    }
}
