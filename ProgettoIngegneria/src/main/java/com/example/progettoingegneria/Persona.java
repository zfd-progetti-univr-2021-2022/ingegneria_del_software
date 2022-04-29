package com.example.progettoingegneria;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


abstract class Persona implements PersonaInterface {

    private String nome;
	private String cognome;
	private String luogoNascita;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataNascita;

	private String nazionalita;
    private String indirizzoEmail;
    private String numeroTelefono;

    protected Persona(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                      String nazionalita, String indirizzoEmail, String numeroTelefono){
        this.nome = nome;
        this.cognome = cognome;
        this.luogoNascita = luogoNascita;
        this.dataNascita = dataNascita;
        this.nazionalita = nazionalita;
        this.indirizzoEmail = indirizzoEmail;
        this.numeroTelefono = numeroTelefono;
    }

    protected String getNome(){
        return this.nome;
    }

    protected String getCognome(){
        return this.cognome;
    }

    protected String getLuogoNascita(){
        return this.luogoNascita;
    }

    protected LocalDate getDataNascita(){
        return this.dataNascita;
    }

    protected String getNazionalita(){
        return this.nazionalita;
    }

    protected String getIndirizzoEmail(){
        return this.indirizzoEmail;
    }

    protected String getNumeroTelefono(){
        return this.numeroTelefono;
    }

    @Override
    public String asJSON() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String result = mapper.writeValueAsString(this);
        return result;
    }
}
