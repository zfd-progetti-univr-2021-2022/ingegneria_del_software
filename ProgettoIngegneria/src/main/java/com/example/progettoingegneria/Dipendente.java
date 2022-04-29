package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;

import java.time.LocalDate;


public class Dipendente extends Persona{

    private final String tipo = "dipendente";

    private static final Validator<Dipendente> validator = ValidatorBuilder.<Dipendente>of()
        .constraint(Dipendente::getUsername, "username", c -> c.notNull())
        .constraint(Dipendente::getPassword, "password", c -> c.notNull())
        .build();

    private String username;
    private String password;

    private Dipendente(String nome, String cognome, String luogoNascita, LocalDate dataNascita, String nazionalita,
                       String indirizzoEmail, String numeroTelefono, String username, String password){

        super(nome, cognome, luogoNascita, dataNascita, nazionalita, indirizzoEmail, numeroTelefono);

        if (username == null || password == null) {
            throw new IllegalArgumentException("username e password non possono essere nulli");
        }

        this.username = username;
        this.password = password;
    }

    public static Dipendente of(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                                String nazionalita, String indirizzoEmail, String numeroTelefono,
                                String username, String password){

        return new Dipendente(
            nome,
            cognome,
            luogoNascita,
            dataNascita,
            nazionalita,
            indirizzoEmail,
            numeroTelefono,
            username,
            password
        );
    }

    protected String getUsername(){
        return this.username;
    }

    protected String getPassword(){
        return this.password;
    }

    public ConstraintViolations validate(){
        ConstraintViolations violations = Dipendente.validator.validate(this);
        return violations;
    }
}
