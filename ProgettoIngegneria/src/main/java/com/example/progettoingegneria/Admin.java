package com.example.progettoingegneria;

import java.time.LocalDate;

public class Admin extends Dipendente{
    /** Tipo di persona nel sistema: utile per l'output JSON */
    private final String tipo = "admin";
    /** Booleano che indica se e' admin **/
    private final boolean isAdmin = true;

    /**
     * Costruttore che crea oggetto che rappresenta un admin.
     *
     * @param nome Nome admin
     * @param cognome Cognome
     * @param luogoNascita Luogo di nascita dipendente
     * @param dataNascita Data nascita dipendente
     * @param nazionalita Nazionalita' dipendente
     * @param indirizzoEmail Indirizzo email
     * @param numeroTelefono NUmero telefono
     * @param username Username
     * @param password Password
     * @param codiceFiscale Codice fiscale
     */
    private Admin(String nome, String cognome, String luogoNascita, LocalDate dataNascita, String nazionalita,
                       String indirizzoEmail, String numeroTelefono, String username, String password, String codiceFiscale){

        super(nome, cognome, luogoNascita, dataNascita, nazionalita, indirizzoEmail, numeroTelefono, username, password, codiceFiscale);
    }

    /**
     * Costruttore usato da jackson per
     * creare gli oggetti a partire da JSON.
     */
    public Admin(){
        super();
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
     * @param codiceFiscale Codice fiscale
     * @return Oggetto che rappresenta un dipendente
     */
    public static Admin of(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                                String nazionalita, String indirizzoEmail, String numeroTelefono,
                                String username, String password, String codiceFiscale){
        return new Admin(
            nome,
            cognome,
            luogoNascita,
            dataNascita,
            nazionalita,
            indirizzoEmail,
            numeroTelefono,
            username,
            password,
            codiceFiscale
        );
    }

    /**
     * Restituisce vero per gli oggetti Admin.
     * @return true
     */
    public boolean isAdmin(){
        return true;
    }
}
