/**
 * Definisce classe astratta che rappresenta una persona nel sistema.
 */

package com.example.progettoingegneria;

import java.time.LocalDate;
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
abstract class Persona implements PersonaInterface {

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
                .lessThanOrEqual(20)       // lunghezza massima 20: 17 e' il massimo qui https://www.dizy.com/it/lista/6017297130979328
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
                    if (s.length() > 20)
                        return false;

                    return true;
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
     */
    protected Persona(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                      String nazionalita, String indirizzoEmail, String numeroTelefono){

        if (nome == null || cognome == null || luogoNascita == null ||
            dataNascita == null || nazionalita == null || indirizzoEmail == null){
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
    }

    public Persona() {}

    /**
     * Restituisce nome della persona.
     * @return Nome
     */
    protected String getNome(){
        return this.nome;
    }

    protected void setNome(String nome){
        this.nome = nome;
    }

    /**
     * Restituisce cognome della persona.
     * @return Cognome
     */
    protected String getCognome(){
        return this.cognome;
    }

    protected void setCognome(String cognome){
        this.cognome = cognome;
    }

    /**
     * Restituisce luogo di nascita della persona.
     * @return Luogo di nascita
     */
    protected String getLuogoNascita(){
        return this.luogoNascita;
    }

    protected void setLuogoNascita(String luogoNascita){
        this.luogoNascita = luogoNascita;
    }

    /**
     * Restituisce data di nascita della persona.
     * @return Data di nascita
     */
    protected LocalDate getDataNascita(){
        return this.dataNascita;
    }

    protected void setDataNascita(LocalDate dataNascita){
        this.dataNascita = dataNascita;
    }

    /**
     * Restituisce nazionalita' della persona.
     * @return Nazionalita'
     */
    protected String getNazionalita(){
        return this.nazionalita;
    }

    protected void setNazionalita(String nazionalita){
        this.nazionalita = nazionalita;
    }

    /**
     * Restituisce indirizzo email della persona.
     * @return Indirizzo email
     */
    protected String getIndirizzoEmail(){
        return this.indirizzoEmail;
    }

    protected void setIndirizzoEmail(String indirizzoEmail){
        this.indirizzoEmail = indirizzoEmail;
    }

    /**
     * Restituisce numero di telefono della persona.
     * @return Numero di telefono
     */
    protected String getNumeroTelefono(){
        return this.numeroTelefono;
    }

    protected void setNumeroTelefono(String numeroTelefono){
        this.numeroTelefono = numeroTelefono;
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
        String result = mapper.writeValueAsString(this);
        return result;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore dei dati della persona.
     * @return Violazioni nelle proprieta' oggetto
     */
    public ConstraintViolations validate(){
        ConstraintViolations violations = Persona.validator.validate(this);
        return violations;
    }
}
