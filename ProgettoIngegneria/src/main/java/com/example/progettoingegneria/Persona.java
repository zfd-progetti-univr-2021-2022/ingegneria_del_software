/**
 * Definisce classe astratta che rappresenta una persona nel sistema.
 *
 * TODO: aggiungere codiceFiscale al validator
 */

package com.example.progettoingegneria;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.CustomConstraint;
import am.ik.yavi.core.Validator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Rappresenta una persona generica all'interno del sistema.
 */
public abstract class Persona implements PersonaInterface, Comparable<Persona> {

    /** Nome */
    private String nome;
    /** Cognome */
	private String cognome;
    /** Luogo di nascita */
	private String luogoNascita;

    /** Data di nascita */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataNascita;

    /** Nazionalita' */
	private String nazionalita;
    /** Indirizzo email */
    private String indirizzoEmail;
    /** Numero di telefono */
    private String numeroTelefono;

    /** Codice fiscale */
    private String codiceFiscale;

    /** Oggetto per validare i dati di una persona */
    private static final Validator<Persona> validator = ValidatorBuilder.<Persona>of()
        .constraint(Persona::getNome, "nome",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255)      // lunghezza massima 255
        )
        .constraint(Persona::getCognome, "cognome",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255)      // lunghezza massima 255
        )
        .constraint(Persona::getLuogoNascita, "luogo di nascita",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255)      // lunghezza massima 255
        )
        .constraint(Persona::getDataNascita, "data di nascita",
            c -> c.notNull()               // non puo' essere null
                .past()                    // deve essere nel passato
                .afterOrEqual(() -> LocalDate.of(1900, 1, 1))  // deve essere nato dopo 1 gennaio 1900
                .beforeOrEqual(new Supplier<LocalDate>() {
                    @Override
                    public LocalDate get() {
                        LocalDate today = LocalDate.now();
                        return today.minusYears(16);        // deve avere almeno 16 anni
                    }
                })
        )
        .constraint(Persona::getNazionalita, "nazionalita'",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(50)       // lunghezza massima 50: 17 e' il massimo qui https://www.dizy.com/it/lista/6017297130979328
        )
        .constraint(Persona::getIndirizzoEmail, "email",
            c -> c.notBlank()              // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255)      // lunghezza massima 255
                .email()                   // e' un indirizzo email
        )
        .constraint(Persona::getNumeroTelefono, "telefono",
            c -> c.predicate(new CustomConstraint<String>() {
                @Override
                public String defaultMessageFormat() {
                    return "\"{0}\" must be either null or it must be not empty and have at max 20 length";
                }

                @Override
                public String messageKey() {
                    return "string.numeroTelefono";
                }

                @Override
                public boolean test(String s) {

                    if (s == null){
                        // essendo opzionale, il numero di telefono puo' essere nullo
                        return true;
                    }

                    // se e' stato specificato un numero non puo' essere vuoto o contenere solo spazi
                    if (s.trim().isEmpty())
                        return false;

                    // se e' specificato deve avere al massimo 20 caratteri
                    return s.length() <= 20;
                }
            })
        )
        .build();

    /**
     * Costruttore di oggetto persona.
     *
     * @param nome Nome
     * @param cognome Cognome
     * @param luogoNascita Luogo di nascita
     * @param dataNascita Data di nascita
     * @param nazionalita Nazionalita'
     * @param indirizzoEmail Indirizzo email
     * @param numeroTelefono Numero telefono (opzionale per i lavoratori)
     * @param codiceFiscale Codice fiscale
     */
    protected Persona(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                      String nazionalita, String indirizzoEmail, String numeroTelefono, String codiceFiscale){

        if (nome == null || cognome == null || luogoNascita == null ||
            dataNascita == null || nazionalita == null || indirizzoEmail == null || codiceFiscale == null){
            // numeroTelefono e' opzionale per i lavoratori
            throw new IllegalArgumentException("I parametri non possono essere nulli (ad eccezione del numero di telefono)");
        }

        this.nome = nome;
        this.cognome = cognome;
        this.luogoNascita = luogoNascita;
        this.dataNascita = dataNascita;
        this.nazionalita = nazionalita;
        this.indirizzoEmail = indirizzoEmail;

        this.numeroTelefono = numeroTelefono;

        this.codiceFiscale = codiceFiscale;
    }

    /**
     * Costruttore usato da jackson
     * per creare oggetti a partire da JSON
     */
    public Persona() {}

    /**
     * Restituisce nome della persona.
     * @return Nome
     */
    public String getNome(){
        return this.nome;
    }

    /**
     * Imposta nome della persona
     * @param nome Nome della persona
     */
    protected void setNome(String nome){
        if (nome == null)
            throw new IllegalArgumentException("nome non puo' essere null");
        this.nome = nome;
    }

    /**
     * Restituisce cognome della persona.
     * @return Cognome
     */
    public String getCognome(){
        return this.cognome;
    }

    /**
     * Imposta cognome della persona
     * @param cognome cognome
     */
    protected void setCognome(String cognome){
        if (cognome == null)
            throw new IllegalArgumentException("cognome non puo' essere null");
        this.cognome = cognome;
    }

    /**
     * Restituisce luogo di nascita della persona.
     * @return Luogo di nascita
     */
    public String getLuogoNascita(){
        return this.luogoNascita;
    }

    /**
     * Imposta luogo di nascita
     * @param luogoNascita Luogo di nascita
     */
    protected void setLuogoNascita(String luogoNascita){
        if (luogoNascita == null)
            throw new IllegalArgumentException("luogoNascita non puo' essere null");
        this.luogoNascita = luogoNascita;
    }

    /**
     * Restituisce data di nascita della persona.
     * @return Data di nascita
     */
    public LocalDate getDataNascita(){
        return this.dataNascita;
    }

    /**
     * Imposta data di nascita
     * @param dataNascita data di nascita
     */
    protected void setDataNascita(LocalDate dataNascita){
        if (dataNascita == null)
            throw new IllegalArgumentException("dataNascita non puo' essere null");
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce nazionalita' della persona.
     * @return Nazionalita'
     */
    public String getNazionalita(){
        return this.nazionalita;
    }

    /**
     * Imposta nazionalita'
     * @param nazionalita Nazionalita'
     */
    protected void setNazionalita(String nazionalita){
        if (nazionalita == null)
            throw new IllegalArgumentException("nazionalita non puo' essere null");
        this.nazionalita = nazionalita;
    }

    /**
     * Restituisce indirizzo email della persona.
     * @return Indirizzo email
     */
    public String getIndirizzoEmail(){
        return this.indirizzoEmail;
    }

    /**
     * Imposta indirizzo email
     * @param indirizzoEmail Indirizzo email
     */
    protected void setIndirizzoEmail(String indirizzoEmail){
        if (indirizzoEmail == null)
            throw new IllegalArgumentException("indirizzoEmail non puo' essere null");
        this.indirizzoEmail = indirizzoEmail;
    }

    /**
     * Restituisce numero di telefono della persona.
     * @return Numero di telefono
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
     * Restituisce il codice fiscale.
     * @return Codice fiscale.
     */
    public String getCodiceFiscale(){
        return this.codiceFiscale;
    }

    /**
     * Imposta il codice fiscale.
     * @param codiceFiscale Codice fiscale
     */
    public void setCodiceFiscale(String codiceFiscale){
        if (codiceFiscale == null)
            throw new IllegalArgumentException("codiceFiscale non puo' essere null");
        this.codiceFiscale = codiceFiscale;
    }

    /**
     * Restituisce stringa JSON con le proprieta' dell'oggetto.
     * @return Stringa contenente JSON con le proprieta' dell'oggetto
     * @throws JsonProcessingException Jackson non riesce a convertire l'oggetto in JSON
     */
    @Override
    public String asJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper.writeValueAsString(this);
    }

    /**
     * Restituisce le violazioni rilevate dal validatore dei dati della persona.
     * @return Violazioni nelle proprieta' oggetto
     */
    public List<String> validate(){
        List<String> violationsMessages = new ArrayList<>();
        ConstraintViolations violations = Persona.validator.validate(this);
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
        Persona persona = (Persona) o;
        return this.codiceFiscale.equals(persona.codiceFiscale);
    }

    /**
     * Restituisce hash
     * @return hash
     */
    @Override
    public int hashCode() {
        return this.codiceFiscale.hashCode();
    }

    @Override
    public String toString() {
        return "Persona{" +
            "nome='" + nome + '\'' +
            ", cognome='" + cognome + '\'' +
            ", luogoNascita='" + luogoNascita + '\'' +
            ", dataNascita=" + dataNascita +
            ", nazionalita='" + nazionalita + '\'' +
            ", indirizzoEmail='" + indirizzoEmail + '\'' +
            ", numeroTelefono='" + numeroTelefono + '\'' +
            '}';
    }

    /**
     * Confronta due persone. Una persona viene prima in ordine
     * rispetto ad un altra in base all'ordine alfabetico dei loro codici fiscali
     * @param other Persona da confrontare con this
     * @return Numero negativo se this viene prima di other, 0 se sono uguali, positivo altrimenti se other viene prima di this
     */
    @Override
    public int compareTo(Persona other) {
        return this.getCodiceFiscale().compareTo(other.getCodiceFiscale());
    }
}
