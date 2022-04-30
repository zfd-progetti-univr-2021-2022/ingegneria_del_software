/**
 * Definisce classe astratta che rappresenta una persona nel sistema.
 *
 * TODO: Aggiungere validator e un metodo validate() (?)
 */

package com.example.progettoingegneria;

import java.time.LocalDate;

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
    private final String nome;
    /** Cognome */
	private final String cognome;
    /** Luogo di nascita */
	private final String luogoNascita;

    /** Data di nascita */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate dataNascita;

    /** Nazionalita' */
	private final String nazionalita;
    /** Indirizzo email */
    private final String indirizzoEmail;
    /** Numero di telefono */
    private final String numeroTelefono;

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

    /**
     * Restituisce nome della persona.
     * @return Nome
     */
    protected String getNome(){
        return this.nome;
    }

    /**
     * Restituisce cognome della persona.
     * @return Cognome
     */
    protected String getCognome(){
        return this.cognome;
    }

    /**
     * Restituisce luogo di nascita della persona.
     * @return Luogo di nascita
     */
    protected String getLuogoNascita(){
        return this.luogoNascita;
    }

    /**
     * Restituisce data di nascita della persona.
     * @return Data di nascita
     */
    protected LocalDate getDataNascita(){
        return this.dataNascita;
    }

    /**
     * Restituisce nazionalita' della persona.
     * @return Nazionalita'
     */
    protected String getNazionalita(){
        return this.nazionalita;
    }

    /**
     * Restituisce indirizzo email della persona.
     * @return Indirizzo email
     */
    protected String getIndirizzoEmail(){
        return this.indirizzoEmail;
    }

    /**
     * Restituisce numero di telefono della persona.
     * @return Numero di telefono
     */
    protected String getNumeroTelefono(){
        return this.numeroTelefono;
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
}
