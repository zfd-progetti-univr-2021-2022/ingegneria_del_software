/**
 * Definisce classe che rappresenta un lavoratore.
 * TODO: Migliorare il validator (in particolare per lingueParlate e patenti)
 */
package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe che rappresenta un lavoratore.
 */
@JsonIgnoreProperties(value={ "tipo" }, allowGetters=true)  // ignora "tipo" quando si converte JSON in oggetto
public class Lavoratore extends Persona{
    /** Tipo di persona nel sistema: utile per l'output JSON */
    private final String tipo = "lavoratore";

    /** Indirizzo di residenza */
    private String indirizzoResidenza;
    /** Insieme di esperienze lavorative del lavoratore */
    private Collection<EsperienzaLavorativa> esperienzeLavorative;
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
    */
    private Lavoratore(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                         String nazionalita, String indirizzoEmail, String numeroTelefono,
                         String indirizzoResidenza, Collection<EsperienzaLavorativa> esperienzeLavorative,
                         Collection<Lingua> lingueParlate, Collection<Patente> patenti,
                         boolean automunito, Collection<PeriodoDisponibilita> periodiDisponibilita,
                         Collection<RecapitoUrgenza> recapitiUrgenze) {

        super(nome, cognome, luogoNascita, dataNascita, nazionalita, indirizzoEmail, numeroTelefono);

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
     * @return Oggetto che rappresenta il lavoratore
     */
    public static Lavoratore of(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                                String nazionalita, String indirizzoEmail, String numeroTelefono,
                                String indirizzoResidenza, Collection<EsperienzaLavorativa> esperienzeLavorative,
                                Collection<Lingua> lingueParlate, Collection<Patente> patenti,
                                boolean automunito, Collection<PeriodoDisponibilita> periodiDisponibilita,
                                Collection<RecapitoUrgenza> recapitiUrgenze) {
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
            recapitiUrgenze
        );
    }

    /**
     * Restituisce l'indirizzo di residenza del lavoratore.
     * @return Indirizzo di residenza del lavoratore
     */
    protected String getIndirizzoResidenza(){
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
     */
    protected void setEsperienzeLavorative(Collection<EsperienzaLavorativa> esperienzeLavorative){
        if (esperienzeLavorative == null)
            throw new IllegalArgumentException("esperienzeLavorative non puo' essere null");
        this.esperienzeLavorative = esperienzeLavorative;
    }

    /**
     * Aggiunge una nuova esperienza lavorativa
     * @param esperienzaLavorativa Esperienza lavorativa da aggiungere
     */
    protected void addEsperienzaLavorativa(EsperienzaLavorativa esperienzaLavorativa){
        if (esperienzaLavorativa == null)
            throw new IllegalArgumentException("esperienzaLavorativa non puo' essere null");

        if (!this.esperienzeLavorative.contains(esperienzaLavorativa))
            this.esperienzeLavorative.add(esperienzaLavorativa);
    }

    /**
     * Rimuove una esperienza lavorativa
     * @param esperienzaLavorativa Esperienza lavorativa
     */
    protected void removeEsperienzaLavorativa(EsperienzaLavorativa esperienzaLavorativa){
        if (esperienzaLavorativa == null)
            throw new IllegalArgumentException("esperienzaLavorativa non puo' essere null");

        if (this.esperienzeLavorative.contains(esperienzaLavorativa))
            this.esperienzeLavorative.remove(esperienzaLavorativa);
    }

    /**
     * Restituisce le lingue parlate dal lavoratore
     * @return Lingue parlate dal lavoratore
     */
    protected Collection<Lingua> getLingueParlate(){
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
     */
    protected void addLinguaParlata(Lingua linguaParlata){
        if (linguaParlata == null)
            throw new IllegalArgumentException("linguaParlata non puo' essere null");

        if (!this.lingueParlate.contains(linguaParlata))
            this.lingueParlate.add(linguaParlata);
    }

    /**
     * Rimuove una lingua parlata
     * @param linguaParlata lingua parlata
     */
    protected void removeLinguaParlata(Lingua linguaParlata){
        if (linguaParlata == null)
            throw new IllegalArgumentException("linguaParlata non puo' essere null");

        if (this.lingueParlate.contains(linguaParlata))
            this.lingueParlate.remove(linguaParlata);
    }

    /**
     * Restituisce le patenti del lavoratore
     * @return Patenti del lavoratore
     */
    protected Collection<Patente> getPatenti(){
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
     */
    protected void addPatente(Patente patente){
        if (patente == null)
            throw new IllegalArgumentException("patente non puo' essere null");

        if (!this.patenti.contains(patente))
            this.patenti.add(patente);
    }

    /**
     * Rimuove una patente
     * @param patente patente
     */
    protected void removePatente(Patente patente){
        if (patente == null)
            throw new IllegalArgumentException("patente non puo' essere null");

        if (this.patenti.contains(patente))
            this.patenti.remove(patente);
    }

    /**
     * Restituisce se il lavoratore e' automunito oppure no
     * @return true se e' automunito, false altrimenti
     */
    protected boolean getAutomunito(){
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
    protected Collection<PeriodoDisponibilita> getPeriodiDisponibilita(){
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
     */
    protected void addPeriodoDisponibilita(PeriodoDisponibilita periodoDisponibilita){
        if (periodoDisponibilita == null)
            throw new IllegalArgumentException("periodoDisponibilita non puo' essere null");

        if (!this.periodiDisponibilita.contains(periodoDisponibilita))
            this.periodiDisponibilita.add(periodoDisponibilita);
    }

    /**
     * Rimuove un periodo di disponibilita'
     * @param periodoDisponibilita periodo di disponibilita'
     */
    protected void removePeriodoDisponibilita(PeriodoDisponibilita periodoDisponibilita){
        if (periodoDisponibilita == null)
            throw new IllegalArgumentException("periodoDisponibilita non puo' essere null");

        if (this.periodiDisponibilita.contains(periodoDisponibilita))
            this.periodiDisponibilita.remove(periodoDisponibilita);
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
     */
    protected void addRecapitoUrgenza(RecapitoUrgenza recapitoUrgenza){
        if (recapitoUrgenza == null)
            throw new IllegalArgumentException("recapitoUrgenza non puo' essere null");

        if (!this.recapitiUrgenze.contains(recapitoUrgenza))
            this.recapitiUrgenze.add(recapitoUrgenza);
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
    public ConstraintViolations validate(){
        ConstraintViolations violations = Lavoratore.validator.validate(this);
        violations.addAll(super.validate());  // validator Persona

        for (EsperienzaLavorativa e: esperienzeLavorative) {
            violations.addAll(e.validate());
        }

        for (PeriodoDisponibilita p: periodiDisponibilita) {
            violations.addAll(p.validate());
        }

        for (RecapitoUrgenza r: recapitiUrgenze) {
            violations.addAll(r.validate());
        }

        return violations;
    }

    /**
     * Verifica se this e' uguale a o
     * @param o Oggetto con cui confrontare this
     * @return true se this e' uguale a o, false altrimenti
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Lavoratore that = (Lavoratore) o;
        return automunito == that.automunito && indirizzoResidenza.equals(that.indirizzoResidenza) && esperienzeLavorative.equals(that.esperienzeLavorative) && lingueParlate.equals(that.lingueParlate) && patenti.equals(that.patenti) && periodiDisponibilita.equals(that.periodiDisponibilita) && recapitiUrgenze.equals(that.recapitiUrgenze);
    }

    /**
     * Restituisce hash
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), indirizzoResidenza, esperienzeLavorative, lingueParlate, patenti, automunito, periodiDisponibilita, recapitiUrgenze);
    }
}
