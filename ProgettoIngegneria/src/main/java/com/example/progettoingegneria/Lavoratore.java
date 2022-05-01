/**
 * Definisce classe che rappresenta un lavoratore.
 * TODO: Migliorare il validator (in particolare per lingueParlate e patenti)
 */
package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;

import java.util.Collection;
import java.time.LocalDate;

/**
 * Classe che rappresenta un lavoratore.
 */
public class Lavoratore extends Persona{
    /** Tipo di persona nel sistema: utile per l'output JSON */
    private final String tipo = "lavoratore";

    /** Indirizzo di residenza */
    private final String indirizzoResidenza;
    /** Insieme di esperienze lavorative del lavoratore */
    private final Collection<EsperienzaLavorativa> esperienzeLavorative;
    /** Insieme di lingue parlate dal lavoratore */
    private final Collection<String> lingueParlate;
    /** Insieme di patenti possedute dal lavoratore */
    private final Collection<Patente> patenti;
    /** Indica se il lavoratore e' automunito o meno */
    private final boolean automunito;
    /** Insieme di periodi di disponibilita' del lavoratore */
    private final Collection<PeriodoDisponibilita> periodiDisponibilita;
    /** Insieme di recapiti da usare in caso di emergenza */
    private final Collection<RecapitoUrgenza> recapitiUrgenze;

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
    public Lavoratore(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                         String nazionalita, String indirizzoEmail, String numeroTelefono,
                         String indirizzoResidenza, Collection<EsperienzaLavorativa> esperienzeLavorative,
                         Collection<String> lingueParlate, Collection<Patente> patenti,
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
                                Collection<String> lingueParlate, Collection<Patente> patenti,
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
     * Restituisce le esperienze lavorative del lavoratore.
     * @return Esperienze lavorative
     */
    protected Collection<EsperienzaLavorativa> getEsperienzeLavorative(){
        return this.esperienzeLavorative;
    }

    /**
     * Restituisce le lingue parlate dal lavoratore
     * @return Lingue parlate dal lavoratore
     */
    protected Collection<String> getLingueParlate(){
        return this.lingueParlate;
    }

    /**
     * Restituisce le patenti del lavoratore
     * @return Patenti del lavoratore
     */
    protected Collection<Patente> getPatenti(){
        return this.patenti;
    }

    /**
     * Restituisce se il lavoratore e' automunito oppure no
     * @return true se e' automunito, false altrimenti
     */
    protected boolean getAutomunito(){
        return this.automunito;
    }

    /**
     * Restituisce i periodi di disponibilita' del lavoratore.
     * @return Periodi di disponibilita'
     */
    protected Collection<PeriodoDisponibilita> getPeriodiDisponibilita(){
        return this.periodiDisponibilita;
    }

    /**
     * Restituisce i recapiti da usare in caso di urgenza.
     * @return Recapiti urgenza
     */
    protected Collection<RecapitoUrgenza> getRecapitiUrgenze(){
        return this.recapitiUrgenze;
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
}
