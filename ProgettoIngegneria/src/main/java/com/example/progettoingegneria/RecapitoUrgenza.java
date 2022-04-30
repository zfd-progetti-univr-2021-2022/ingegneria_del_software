/**
 * Definisce classe accessibile solo nel package che rappresenta
 * un recapito da usare in caso di emergenza.
 * TODO: Creare un metodo validate() per restituire l'output del validator?
 */

package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.Validator;

/**
 * Classe accessibile solo nel package che rappresenta un recapito da usare in caso di emergenza.
 *
 * Per istanziare un oggetto utilizzare il factory method of().
 */
class RecapitoUrgenza {

    /** Nome recapito */
    private final String nome;
    /** Cognome recapito */
    private final String cognome;
    /** Numero telefono recapito */
    private final String numeroTelefono;
    /** Indirizzo email recapito */
    private final String indirizzoEmail;

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
    protected String getNome(){
        return this.nome;
    }

    /**
     * Restituisce cognome.
     * @return Stringa con cognome
     */
    protected String getCognome(){
        return this.cognome;
    }

    /**
     * Restituisce numero di telefono
     * @return Stringa con numero di telefono
     */
    protected String getNumeroTelefono(){
        return this.numeroTelefono;
    }

    /**
     * Restituisce indirizzo email.
     * @return Stringa con indirizzo email
     */
    protected String getIndirizzoEmail(){
        return this.indirizzoEmail;
    }
}
