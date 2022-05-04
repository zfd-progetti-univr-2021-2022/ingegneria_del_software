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
import java.util.Arrays;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

public class ManagementSystem {

    private static ManagementSystem instance = null;
    private Persona loggedInUser = null;
    private final Collection<Dipendente> dipendenti = new ArrayList<Dipendente>();
    private final Collection<Lavoratore> lavoratori = new ArrayList<Lavoratore>();
    private final Collection<Admin> admins = new ArrayList<Admin>();
    private final ObjectMapper objectMapper = JsonMapper.builder().enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER).build();
    private final String resourcePath = new File(ManagementSystem.class.getResource("dipendenti.json").getFile()).toPath().getParent().toString();
    private final File dipendentiFile = Paths.get(resourcePath, "dipendenti.json").toFile();
    private final File lavoratoriFile = Paths.get(resourcePath, "lavoratori.json").toFile();
    private final File adminFile = Paths.get(resourcePath, "admin.json").toFile();

    private ManagementSystem(){
        objectMapper.findAndRegisterModules();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            Logger.info("Inizio setup iniziale");
            this.initialSetup();
            Logger.debug("Fine setup iniziale");
        }
        catch (IOException e){
            System.out.println(e);
        }
        catch (URISyntaxException e){
            System.out.println(e);
        }
    }

    public static ManagementSystem getInstance(){
        if (ManagementSystem.instance == null){
            ManagementSystem.instance = new ManagementSystem();
        }
        return ManagementSystem.instance;
    }

    private void initialSetup() throws IOException, URISyntaxException {
        Logger.debug("Importo file JSON");
        this.importJSONDipendenti(dipendentiFile);
        this.importJSONLavoratori(lavoratoriFile);
        this.importJSONAdmin(adminFile);
    }

    private void importJSONDipendenti(File filePath) throws IOException {
        Logger.debug("Importo dipendenti dal file JSON {}", filePath.toString());
        List<Dipendente> dipendenti = Arrays.asList(objectMapper.readValue(filePath, Dipendente[].class));
        this.dipendenti.addAll(dipendenti);
    }

    private void importJSONLavoratori(File filePath) throws IOException {
        Logger.debug("Importo lavoratori dal file JSON {}", filePath.toString());
        List<Lavoratore> lavoratori = Arrays.asList(objectMapper.readValue(filePath, Lavoratore[].class));
        this.lavoratori.addAll(lavoratori);
    }

    private void importJSONAdmin(File filePath) throws IOException {
        Logger.debug("Importo admin dal file JSON {}", filePath.toString());
        List<Admin> admins = Arrays.asList(objectMapper.readValue(filePath, Admin[].class));
        this.admins.addAll(admins);
    }

    private void exportJSONDipendenti(File filePath) throws IOException, URISyntaxException {
        Logger.debug("Esporto dipendenti su file JSON {}", filePath.toString());
        PrintWriter writer = new PrintWriter(filePath);
        objectMapper.writeValue(writer, this.dipendenti);
        writer.close();
    }

    private void exportJSONLavoratori(File filePath) throws IOException, URISyntaxException {
        Logger.debug("Esporto lavoratori su file JSON {}", filePath.toString());
        PrintWriter writer = new PrintWriter(filePath);
        objectMapper.writeValue(writer, this.lavoratori);
        writer.close();
    }

    private void exportJSONAdmins(File filePath) throws IOException, URISyntaxException {
        Logger.debug("Esporto admin su file JSON {}", filePath.toString());
        PrintWriter writer = new PrintWriter(filePath);
        objectMapper.writeValue(writer, this.admins);
        writer.close();
    }

    public void commitChanges() throws IOException, URISyntaxException {
        Logger.info("Memorizzo modifiche sui file JSON");
        exportJSONDipendenti(dipendentiFile);
        exportJSONLavoratori(lavoratoriFile);
        exportJSONAdmins(adminFile);
    }

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

    public boolean logout(){
        if (loggedInUser == null){
            return false;
        }
        Logger.info("L'utente {} vuole effettuare il logout", ((Dipendente)loggedInUser).getUsername());
        loggedInUser = null;
        return true;
    }

    public boolean addLavoratore(Lavoratore lavoratore) throws IOException, URISyntaxException {
        if (loggedInUser == null)
            return false;

        Logger.info("Un utente vuole iscrivere il lavoratore {} {}", lavoratore.getNome(), lavoratore.getCognome());

        if (!loggedInUser.isDipendente()) {
            Logger.info("L'utente non ha i permessi necessari per iscrivere il lavoratore");
            return false;
        }

        Dipendente utente = (Dipendente) loggedInUser;

        if (lavoratore.validate().size() == 0 ) {
            if ((!lavoratori.contains(lavoratore)) && lavoratori.add(lavoratore)){
                commitChanges();
                Logger.info("L'utente {} ha aggiunto il lavoratore {} {}", utente.getUsername(), lavoratore.getNome(), lavoratore.getCognome());
                return true;
            }
            else{
                Logger.info("Non e' stato possibile aggiungere il lavoratore: possibile duplicato");
            }
        }
        else{
            Logger.info("Alcuni dati del lavoratore sono stati scritti non correttamente");
        }
        return false;
    }

    public boolean removeLavoratore(Lavoratore lavoratore) throws IOException, URISyntaxException {
        if (loggedInUser == null)
            return false;

        Logger.info("Un utente vuole rimuovere il lavoratore {} {}", lavoratore.getNome(), lavoratore.getCognome());

        if (!loggedInUser.isDipendente())
            return false;

        Dipendente utente = (Dipendente) loggedInUser;

        if (this.lavoratori.contains(lavoratore) && lavoratori.remove(lavoratore)) {
            commitChanges();
            Logger.info("L' utente {} vuole rimuovere il lavoratore {} {}", utente.getUsername(), lavoratore.getNome(), lavoratore.getCognome());
            return true;
        }
        else{
            Logger.info("Non e' stato possibile rimuovere il lavoratore {} {}", lavoratore.getNome(), lavoratore.getCognome());
        }

        return false;
    }

    public boolean addDipendente(Dipendente dipendente) throws IOException, URISyntaxException {
        if (loggedInUser == null)
            return false;

        if (!loggedInUser.isAdmin())
            return false;

        if (dipendente.validate().size() == 0 ) {
            if (dipendenti.add(dipendente)){
                commitChanges();
                return true;
            }
        }
        return false;
    }

    public boolean removeDipendente(Dipendente dipendente) throws IOException, URISyntaxException {
        if (loggedInUser == null)
            return false;

        if (!loggedInUser.isAdmin())
            return false;

        if (dipendenti.remove(dipendente)) {
            commitChanges();
            return true;
        }
        return false;
    }

    public boolean addAdmin(Admin admin) throws IOException, URISyntaxException {
        if (loggedInUser == null)
            return false;

        if (!loggedInUser.isAdmin())
            return false;

        if (admin.validate().size() == 0 ) {
            if (admins.add(admin)){
                commitChanges();
                return true;
            }
        }
        return false;
    }

    public boolean removeAdmin(Admin admin) throws IOException, URISyntaxException {
        if (loggedInUser == null)
            return false;

        if (!loggedInUser.isAdmin())
            return false;

        if (admins.size() <= 1)
            return false;

        if (admins.remove(admin)) {
            commitChanges();
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "ManagementSystem{" +
            "Dipendenti" + dipendenti.toString() +
            "Lavoratori" + lavoratori.toString() +
            "}";
    }
}
