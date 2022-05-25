/**
 * Definisce classe accessibile solo nel package che rappresenta
 * un recapito da usare in caso di emergenza.
 */

package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Classe accessibile solo nel package che rappresenta un recapito da usare in caso di emergenza.
 *
 * Per istanziare un oggetto utilizzare il factory method of().
 */
public class RecapitoUrgenza {

    /** Nome recapito */
    private String nome;
    /** Cognome recapito */
    private String cognome;
    /** Numero telefono recapito */
    private String numeroTelefono;
    /** Indirizzo email recapito */
    private String indirizzoEmail;

    /** Oggetto per validare i recapiti */
    private static final Validator<RecapitoUrgenza> validator = ValidatorBuilder.<RecapitoUrgenza>of()
        .constraint(RecapitoUrgenza::getNome, "nome",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255) // lunghezza massima 255
        )
        .constraint(RecapitoUrgenza::getCognome, "cognome",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255) // lunghezza massima 255
        )
        .constraint(RecapitoUrgenza::getNumeroTelefono, "telefono",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(20)  // lunghezza massima 20: al momento 15 e' il massimo https://en.wikipedia.org/wiki/E.164
        )
        .constraint(RecapitoUrgenza::getIndirizzoEmail, "email",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255) // lunghezza massima 255
                .email()                   // e' un indirizzo email
        )
        .build();


    /**
     * Costruttore recapito di emergenza.
     *
     * @param nome Nome del recapito
     * @param cognome Cognome del recapito
     * @param numeroTelefono Numero di telefono del recapito
     * @param indirizzoEmail Indirizzo email del recapito
     */
    private RecapitoUrgenza(String nome, String cognome, String numeroTelefono, String indirizzoEmail){

        if (nome == null || cognome == null || numeroTelefono == null || indirizzoEmail == null){
            throw new IllegalArgumentException("I parametri non possono essere nulli");
        }

        this.nome = nome;
        this.cognome = cognome;
        this.numeroTelefono = numeroTelefono;
        this.indirizzoEmail = indirizzoEmail;
    }

    /**
     * Costruttore usato da jackson
     * per creare oggetti a partire da JSON
     */
    public RecapitoUrgenza(){}

    /**
     * Factory method per i recapiti di emergenza.
     *
     * @param nome Nome del recapito
     * @param cognome Cognome del recapito
     * @param numeroTelefono Numero di telefono del recapito
     * @param indirizzoEmail Indirizzo email del recapito
     * @return Recapito di emergenza
     */
    public static RecapitoUrgenza of(String nome, String cognome, String numeroTelefono, String indirizzoEmail){
        return new RecapitoUrgenza(
            nome,
            cognome,
            numeroTelefono,
            indirizzoEmail
        );
    }

    /**
     * Restituisce nome.
     * @return Stringa con nome
     */
    public String getNome(){
        return this.nome;
    }

    /**
     * Imposta nome
     * @param nome nome
     */
    protected void setNome(String nome){
        if (nome == null)
            throw new IllegalArgumentException("nome non puo' essere null");
        this.nome = nome;
    }

    /**
     * Restituisce cognome.
     * @return Stringa con cognome
     */
    public String getCognome(){
        return this.cognome;
    }

    /**
     * Imposta cognome
     * @param cognome cognome
     */
    protected void setCognome(String cognome){
        if (cognome == null)
            throw new IllegalArgumentException("cognome non puo' essere null");
        this.cognome = cognome;
    }

    /**
     * Restituisce numero di telefono
     * @return Stringa con numero di telefono
     */
    public String getNumeroTelefono(){
        return this.numeroTelefono;
    }

    /**
     * Imposta numero di telefono
     * @param numeroTelefono numero di telefono
     */
    protected void setNumeroTelefono(String numeroTelefono){
        if (numeroTelefono == null)
            throw new IllegalArgumentException("numeroTelefono non puo' essere null");
        this.numeroTelefono = numeroTelefono;
    }

    /**
     * Restituisce indirizzo email.
     * @return Stringa con indirizzo email
     */
    public String getIndirizzoEmail(){
        return this.indirizzoEmail;
    }

    /**
     * Imposta indirizzo email
     * @param indirizzoEmail indirizzo email
     */
    protected void setIndirizzoEmail(String indirizzoEmail){
        if (indirizzoEmail == null)
            throw new IllegalArgumentException("indirizzoEmail non puo' essere null");
        this.indirizzoEmail = indirizzoEmail;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore del recapito urgenza.
     * @return Violazioni nelle proprieta' oggetto
     */
    public List<String> validate(){
        List<String> violationsMessages = new ArrayList<>();
        ConstraintViolations violations = RecapitoUrgenza.validator.validate(this);
        violations.forEach(v -> violationsMessages.add(v.message()));
        return violationsMessages;
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
        RecapitoUrgenza that = (RecapitoUrgenza) o;
        return nome.equals(that.nome) && cognome.equals(that.cognome) && numeroTelefono.equals(that.numeroTelefono) && indirizzoEmail.equals(that.indirizzoEmail);
    }

    /**
     * Restituisce hash
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, numeroTelefono, indirizzoEmail);
    }
}
