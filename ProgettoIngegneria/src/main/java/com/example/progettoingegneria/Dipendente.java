/**
 * Definisce classe che rappresenta un dipendente.
 * TODO: Migliorare il validator
 */
package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;

import java.time.LocalDate;

/**
 * Crea oggetti che rappresentano un dipendente.
 *
 * Per creare un oggetto utilizzare il factory method of().
 */
public class Dipendente extends Persona{

    /** Tipo di persona nel sistema */
    private final String tipo = "dipendente";
    /** Username del dipendente */
    private final String username;
    /** Password del dipendente */
    private final String password;

    /** Validatore per il dipendente */
    private static final Validator<Dipendente> validator = ValidatorBuilder.<Dipendente>of()
        .constraint(Dipendente::getUsername, "username",
            c -> c.notBlank()  // non puo' essere: nullo, vuoto, contenere solo spazi
        )
        .constraint(Dipendente::getPassword, "password",
            c -> c.notBlank()                 // non puo' essere: nullo, vuoto, contenere solo spazi
                .greaterThanOrEqual(12)  // deve avere almeno 12 caratteri
                .password(
                    policy -> policy
                        .uppercase()          // deve avere almeno una lettera maiuscola
                        .lowercase()          // almeno una lettera minuscola
                        .numbers()            // almeno un numero
                        .symbols()            // almeno un simbolo
                        .build()
                )
        )
        .build();

    /**
     * Costruttore che crea oggetto che rappresenta un dipendente.
     *
     * @param nome Nome dipendente
     * @param cognome Cognome
     * @param luogoNascita Luogo di nascita dipendente
     * @param dataNascita Data nascita dipendente
     * @param nazionalita Nazionalita' dipendente
     * @param indirizzoEmail Indirizzo email
     * @param numeroTelefono NUmero telefono
     * @param username Username
     * @param password Password
     */
    private Dipendente(String nome, String cognome, String luogoNascita, LocalDate dataNascita, String nazionalita,
                       String indirizzoEmail, String numeroTelefono, String username, String password){

        super(nome, cognome, luogoNascita, dataNascita, nazionalita, indirizzoEmail, numeroTelefono);

        // nota: il controllo del numero di telefono viene fatto qui perche' il lavoratore,
        // che eredita da Persona, puo' avere numero di telefono nullo
        if (numeroTelefono == null || username == null || password == null) {
            throw new IllegalArgumentException("numero di telefono, username e password non possono essere nulli");
        }

        this.username = username;
        this.password = password;
    }

    /**
     * Factory method che restituisce un oggetto che rappresenta un dipendente.
     *
     * @param nome Nome dipendente
     * @param cognome Cognome
     * @param luogoNascita Luogo di nascita dipendente
     * @param dataNascita Data nascita dipendente
     * @param nazionalita Nazionalita' dipendente
     * @param indirizzoEmail Indirizzo email
     * @param numeroTelefono NUmero telefono
     * @param username Username
     * @param password Password
     * @return Oggetto che rappresenta un dipendente
     */
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

    /**
     * Restituisce lo username del dipendente
     * @return Username dipendente
     */
    protected String getUsername(){
        return this.username;
    }

    /**
     * Restituisce password del dipendente
     * @return Password del dipendente
     */
    protected String getPassword(){
        return this.password;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore dell'oggetto.
     * TODO: Includere in violations l'output del validator di Persona per restituire tutti i problemi dei dati del dipendente
     * @return Violazioni nelle proprieta' oggetto
     */
    public ConstraintViolations validate(){
        ConstraintViolations violations = Dipendente.validator.validate(this);
        return violations;
    }
}
