/**
 * Definisce classe che rappresenta un lavoratore.
 * TODO: Migliorare il validator (in particolare per lingueParlate e patenti)
 */
package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

/**
 * Classe che rappresenta un lavoratore.
 */
@JsonIgnoreProperties(value={ "tipo", "isAdmin", "admin", "dipendente", "uniqueEsperienzaLavorativaId"}, allowGetters=true)  // ignora "tipo" quando si converte JSON in oggetto
public class Lavoratore extends Persona{
    /** Tipo di persona nel sistema: utile per l'output JSON */
    private final String tipo = "lavoratore";

    /** Indirizzo di residenza */
    private String indirizzoResidenza;
    /** Insieme di esperienze lavorative del lavoratore */
    private Collection<EsperienzaLavorativa> esperienzeLavorative = new ArrayList<>();
    /** Insieme di lingue parlate dal lavoratore */
    private Collection<Lingua> lingueParlate;
    /** Insieme di patenti possedute dal lavoratore */
    private Collection<Patente> patenti;
    /** Indica se il lavoratore e' automunito o meno */
    private boolean automunito;
    /** Insieme di periodi di disponibilita' del lavoratore */
    private Collection<PeriodoDisponibilita> periodiDisponibilita;
    /** Insieme di recapiti da usare in caso di emergenza */
    private Collection<RecapitoUrgenza> recapitiUrgenze;

    /** Identificativo univoco esperienza lavorativa */
    private int uniqueEsperienzaLavorativaId = 0;

    /** Validatore dati di un lavoratore */
    private static final Validator<Lavoratore> validator = ValidatorBuilder.<Lavoratore>of()
        .constraint(Lavoratore::getIndirizzoResidenza, "indirizzoResidenza",
            c -> c.notBlank()  // non puo' essere: nullo, vuoto, contenere solo spazi
                .lessThanOrEqual(255)
        )
        .constraint(Lavoratore::getEsperienzeLavorative, "esperienzeLavorative",
            c -> c.notNull()  // non puo' essere nullo
        )
        .constraint(Lavoratore::getLingueParlate, "lingue parlate",
            c -> c.notNull()  // non puo' essere nullo
        )
        .constraint(Lavoratore::getPatenti, "patenti",
            c -> c.notNull()  // non puo' essere nullo
        )
        .constraint(Lavoratore::getPeriodiDisponibilita, "periodi disponibilita'",
            c -> c.notNull()  // non puo' essere nullo
        )
        .constraint(Lavoratore::getRecapitiUrgenze, "recapiti urgenze",
            c -> c.notEmpty()  // non puo' essere ne nullo ne vuoto
        )
        .build();

    /**
     * Crea un oggetto che rappresenta un lavoratore.
     *
     * @param nome Nome lavoratore
     * @param cognome Cognome lavoratore
     * @param luogoNascita Luogo di nascita
     * @param dataNascita Data di nastica
     * @param nazionalita Nazionalita'
     * @param indirizzoEmail Indirizzo email
     * @param numeroTelefono Numero di telefono
     * @param indirizzoResidenza Indirizzo di Residenza
     * @param esperienzeLavorative Esperienze lavorative
     * @param lingueParlate Lingue parlate
     * @param patenti Patenti di guida
     * @param automunito Se e' automunito o meno
     * @param periodiDisponibilita Periodi di disponibilita' lavorativa
     * @param recapitiUrgenze Recapiti da usare in caso di urgenza
     * @param codiceFiscale Codice fiscale
    */
    private Lavoratore(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                         String nazionalita, String indirizzoEmail, String numeroTelefono,
                         String indirizzoResidenza, Collection<EsperienzaLavorativa> esperienzeLavorative,
                         Collection<Lingua> lingueParlate, Collection<Patente> patenti,
                         boolean automunito, Collection<PeriodoDisponibilita> periodiDisponibilita,
                         Collection<RecapitoUrgenza> recapitiUrgenze, String codiceFiscale) {

        super(nome, cognome, luogoNascita, dataNascita, nazionalita, indirizzoEmail, numeroTelefono, codiceFiscale);

        if (indirizzoResidenza == null || esperienzeLavorative == null || lingueParlate == null
            || patenti == null || periodiDisponibilita == null || recapitiUrgenze == null){
            throw new IllegalArgumentException("I parametri non possono essere nulli");
        }

        this.indirizzoResidenza = indirizzoResidenza;
        this.esperienzeLavorative = esperienzeLavorative;
        this.lingueParlate = lingueParlate;
        this.patenti = patenti;
        this.automunito = automunito;
        this.periodiDisponibilita = periodiDisponibilita;
        this.recapitiUrgenze = recapitiUrgenze;
    }

    /**
     * Costruttore usato da jackson per
     * creare oggetti a partire da JSON
     */
    public Lavoratore(){
        super();
    }

    /**
     * Factory method che restituisce un oggetto che rappresenta un lavoratore.
     *
     * @param nome Nome lavoratore
     * @param cognome Cognome lavoratore
     * @param luogoNascita Luogo di nascita
     * @param dataNascita Data di nastica
     * @param nazionalita Nazionalita'
     * @param indirizzoEmail Indirizzo email
     * @param numeroTelefono Numero di telefono
     * @param indirizzoResidenza Indirizzo di Residenza
     * @param esperienzeLavorative Esperienze lavorative
     * @param lingueParlate Lingue parlate
     * @param patenti Patenti di guida
     * @param automunito Se e' automunito o meno
     * @param periodiDisponibilita Periodi di disponibilita' lavorativa
     * @param recapitiUrgenze Recapiti da usare in caso di urgenza
     * @param codiceFiscale Codice fiscale
     * @return Oggetto che rappresenta il lavoratore
     */
    public static Lavoratore of(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                                String nazionalita, String indirizzoEmail, String numeroTelefono,
                                String indirizzoResidenza, Collection<EsperienzaLavorativa> esperienzeLavorative,
                                Collection<Lingua> lingueParlate, Collection<Patente> patenti,
                                boolean automunito, Collection<PeriodoDisponibilita> periodiDisponibilita,
                                Collection<RecapitoUrgenza> recapitiUrgenze, String codiceFiscale) {
        return new Lavoratore(
            nome,
            cognome,
            luogoNascita,
            dataNascita,
            nazionalita,
            indirizzoEmail,
            numeroTelefono,
            indirizzoResidenza,
            esperienzeLavorative,
            lingueParlate,
            patenti,
            automunito,
            periodiDisponibilita,
            recapitiUrgenze,
            codiceFiscale
        );
    }

    /**
     * Restituisce l'indirizzo di residenza del lavoratore.
     * @return Indirizzo di residenza del lavoratore
     */
    public String getIndirizzoResidenza(){
        return this.indirizzoResidenza;
    }

    /**
     * Imposta indirizzo di residenza
     * @param indirizzoResidenza indirizzo di residenza
     */
    protected void setIndirizzoResidenza(String indirizzoResidenza){
        if (indirizzoResidenza == null)
            throw new IllegalArgumentException("indirizzoResidenza non puo' essere null");
        this.indirizzoResidenza = indirizzoResidenza;
    }

    /**
     * Restituisce le esperienze lavorative del lavoratore.
     * @return Esperienze lavorative
     */
    protected Collection<EsperienzaLavorativa> getEsperienzeLavorative(){
        return this.esperienzeLavorative;
    }

    /**
     * Imposta esperienze lavorative.
     * @param esperienzeLavorative Esperienze lavorative
     * @return true se tutte le esperienze lavorative sono state aggiunte correttamente
     */
    protected boolean setEsperienzeLavorative(Collection<EsperienzaLavorativa> esperienzeLavorative){
        if (esperienzeLavorative == null)
            throw new IllegalArgumentException("esperienzeLavorative non puo' essere null");

        boolean success = true;
        for (EsperienzaLavorativa esperienzaLavorativa: esperienzeLavorative) {
            if (!addEsperienzaLavorativa(esperienzaLavorativa)){
                success = false;
            }
        }
        return success;
    }

    /**
     * Aggiunge una nuova esperienza lavorativa
     * @param esperienzaLavorativa Esperienza lavorativa da aggiungere
     * @return true se esperienzaLavorativa e' stata aggiunta
     */
    protected boolean addEsperienzaLavorativa(EsperienzaLavorativa esperienzaLavorativa){
        if (esperienzaLavorativa == null)
            throw new IllegalArgumentException("esperienzaLavorativa non puo' essere null");

        esperienzaLavorativa.setId(uniqueEsperienzaLavorativaId);
        uniqueEsperienzaLavorativaId++;

        if (!this.esperienzeLavorative.contains(esperienzaLavorativa)) {
            return this.esperienzeLavorative.add(esperienzaLavorativa);
        }
        return false;
    }

    /**
     * Rimuove una esperienza lavorativa
     * @param esperienzaLavorativaId Identificativo esperienza lavorativa
     * @return true se esperienzaLavorativa e' stata rimossa
     */
    protected boolean removeEsperienzaLavorativa(int esperienzaLavorativaId){
        for (EsperienzaLavorativa esperienzaLavorativa : esperienzeLavorative) {
            if (esperienzaLavorativa.getId() == esperienzaLavorativaId){
                return this.esperienzeLavorative.remove(esperienzaLavorativa);
            }
        }
        return false;
    }

    /**
     * Restituisce una lista di tutte le mansioni univoche svolte dal lavoratore.
     * @return Lista di tutte le mansioni univoche svolte dal lavoratore.
     */
    public Collection<String> getMansioni(){
        HashSet<String> mansioni = new HashSet<>();
        for (EsperienzaLavorativa esperienzaLavorativa: esperienzeLavorative){
            mansioni.addAll(esperienzaLavorativa.getMansioniSvolte());
        }
        return mansioni;
    }

    /**
     * Restituisce le lingue parlate dal lavoratore
     * @return Lingue parlate dal lavoratore
     */
    public Collection<Lingua> getLingueParlate(){
        return this.lingueParlate;
    }

    /**
     * Imposta lingue parlate.
     * @param lingueParlate lingue parlate
     */
    protected void setLingueParlate(Collection<Lingua> lingueParlate){
        if (lingueParlate == null)
            throw new IllegalArgumentException("lingueParlate non puo' essere null");
        this.lingueParlate = lingueParlate;
    }

    /**
     * Aggiunge una lingua parlata
     * @param linguaParlata Lingua parlata
     * @return true se linguaParlata e' stata aggiunta
     */
    protected boolean addLinguaParlata(Lingua linguaParlata){
        if (linguaParlata == null)
            throw new IllegalArgumentException("linguaParlata non puo' essere null");

        if (!this.lingueParlate.contains(linguaParlata)) {
            return this.lingueParlate.add(linguaParlata);
        }
        return false;
    }

    /**
     * Rimuove una lingua parlata
     * @param linguaParlata lingua parlata
     * @return true se linguaParlata e' stata rimossa
     */
    protected boolean removeLinguaParlata(Lingua linguaParlata){
        if (linguaParlata == null)
            throw new IllegalArgumentException("linguaParlata non puo' essere null");

        return this.lingueParlate.remove(linguaParlata);
    }

    /**
     * Restituisce le patenti del lavoratore
     * @return Patenti del lavoratore
     */
    public Collection<Patente> getPatenti(){
        return this.patenti;
    }

    /**
     * Imposta patenti
     * @param patenti patenti
     */
    protected void setPatenti(Collection<Patente> patenti){
        if (patenti == null)
            throw new IllegalArgumentException("patenti non puo' essere null");
        this.patenti = patenti;
    }

    /**
     * Aggiunge una patente
     * @param patente patente
     * @return true se patente e' stata aggiunta
     */
    protected boolean addPatente(Patente patente){
        if (patente == null)
            throw new IllegalArgumentException("patente non puo' essere null");

        if (!this.patenti.contains(patente)) {
            return this.patenti.add(patente);
        }
        return false;
    }

    /**
     * Rimuove una patente
     * @param patente patente
     * @return true se patente e' stato rimossa
     */
    protected boolean removePatente(Patente patente){
        if (patente == null)
            throw new IllegalArgumentException("patente non puo' essere null");

        return this.patenti.remove(patente);
    }

    /**
     * Restituisce se il lavoratore e' automunito oppure no
     * @return true se e' automunito, false altrimenti
     */
    public boolean getAutomunito(){
        return this.automunito;
    }

    /**
     * Imposta se il lavoratore e' automunito o no
     * @param automunito true se il lavoratore e' automunito, false altrimenti
     */
    protected void setAutomunito(boolean automunito){
        this.automunito = automunito;
    }

    /**
     * Restituisce i periodi di disponibilita' del lavoratore.
     * @return Periodi di disponibilita'
     */
    public Collection<PeriodoDisponibilita> getPeriodiDisponibilita(){
        return this.periodiDisponibilita;
    }

    /**
     * Imposta periodi di disponibilita'
     * @param periodiDisponibilita periodi di disponibilita'
     */
    protected void setPeriodiDisponibilita(Collection<PeriodoDisponibilita> periodiDisponibilita){
        if (periodiDisponibilita == null)
            throw new IllegalArgumentException("periodiDisponibilita non puo' essere null");
        this.periodiDisponibilita = periodiDisponibilita;
    }

    /**
     * Aggiunge un periodo di disponibilita'
     * @param periodoDisponibilita periodo di disponibilita'
     * @return true se periodoDisponibilita e' stato aggiunto
     */
    protected boolean addPeriodoDisponibilita(PeriodoDisponibilita periodoDisponibilita){
        if (periodoDisponibilita == null)
            throw new IllegalArgumentException("periodoDisponibilita non puo' essere null");

        if (!this.periodiDisponibilita.contains(periodoDisponibilita)) {
            return this.periodiDisponibilita.add(periodoDisponibilita);
        }
        return false;
    }

    /**
     * Rimuove un periodo di disponibilita'
     * @param periodoDisponibilita periodo di disponibilita'
     * @return true se periodoDisponibilita e' stato rimosso
     */
    protected boolean removePeriodoDisponibilita(PeriodoDisponibilita periodoDisponibilita){
        if (periodoDisponibilita == null)
            throw new IllegalArgumentException("periodoDisponibilita non puo' essere null");

        return this.periodiDisponibilita.remove(periodoDisponibilita);
    }

    /**
     * Restituisce i recapiti da usare in caso di urgenza.
     * @return Recapiti urgenza
     */
    protected Collection<RecapitoUrgenza> getRecapitiUrgenze(){
        return this.recapitiUrgenze;
    }

    /**
     * Imposta recapiti da usare in caso di urgenza
     * @param recapitiUrgenze Recapiti da usare in caso di urgenza
     */
    protected void setRecapitiUrgenze(Collection<RecapitoUrgenza> recapitiUrgenze){
        if (recapitiUrgenze == null)
            throw new IllegalArgumentException("recapitiUrgenze non puo' essere null");
        this.recapitiUrgenze = recapitiUrgenze;
    }

    /**
     * Aggiunge un recapito urgenza.
     * @param recapitoUrgenza recapito urgenza
     * @return true se recapitoUrgenza e' stato aggiunto
     */
    protected boolean addRecapitoUrgenza(RecapitoUrgenza recapitoUrgenza){
        if (recapitoUrgenza == null)
            throw new IllegalArgumentException("recapitoUrgenza non puo' essere null");

        if (!this.recapitiUrgenze.contains(recapitoUrgenza)) {
            return this.recapitiUrgenze.add(recapitoUrgenza);
        }
        return false;
    }

    /**
     * Rimuove un recapito urgenza. Se e' presente un solo recapito nella lista dei recapiti non viene rimosso.
     *
     * @param recapitoUrgenza recapito urgenza
     * @return true se e' stato rimosso il recapito
     */
    protected boolean removeRecapitoUrgenza(RecapitoUrgenza recapitoUrgenza){
        if (recapitoUrgenza == null)
            throw new IllegalArgumentException("recapitoUrgenza non puo' essere null");

        if (this.recapitiUrgenze.size() <= 1)
            return false;

        if (this.recapitiUrgenze.contains(recapitoUrgenza)) {
            this.recapitiUrgenze.remove(recapitoUrgenza);
            return true;
        }

        return false;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore dell'oggetto.
     * TODO: Includere in violations l'output del validator di Persona e di tutti i sotto-validator dei dati del lavoratore
     * @return Violazioni nelle proprieta' oggetto
     */
    @Override
    public List<String> validate(){
        // validator Persona
        List<String> violationsMessages = new ArrayList<>(super.validate());

        // validator Lavoratore
        ConstraintViolations violations = Lavoratore.validator.validate(this);
        violations.forEach(v -> violationsMessages.add(v.message()));

        // validator di ogni esperienza lavorativa
        for (EsperienzaLavorativa e: esperienzeLavorative) {
            violationsMessages.addAll(e.validate());
        }

        // validator di ogni periodo di disponibilita
        for (PeriodoDisponibilita p: periodiDisponibilita) {
            violationsMessages.addAll(p.validate());
        }

        // validator di ogni recapito urgenza
        for (RecapitoUrgenza r: recapitiUrgenze) {
            violationsMessages.addAll(r.validate());
        }

        return violationsMessages;
    }

    /**
     * Un lavoratore non e' Admin
     * @return false
     */
    @Override
    public boolean isAdmin() {
        return false;
    }

    /**
     * Un lavoratore non e' Dipendente (e non e' Admin)
     * @return false
     */
    @Override
    public boolean isDipendente(){
        return false;
    }
}
