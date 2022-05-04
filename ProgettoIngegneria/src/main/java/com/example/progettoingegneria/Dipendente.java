/**
 * Definisce classe che rappresenta un dipendente.
 */
package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Crea oggetti che rappresentano un dipendente.
 *
 * Per creare un oggetto utilizzare il factory method of().
 */
@JsonIgnoreProperties(value={ "tipo", "isAdmin", "admin", "dipendente" }, allowGetters=true)  // ignora "tipo" quando si converte JSON in oggetto
public class Dipendente extends Persona{

    /** Tipo di persona nel sistema: utile per l'output JSON */
    private final String tipo = "dipendente";
    /** Username del dipendente */
    private String username;
    /** Password del dipendente */
    private String password;
    /** Booleano che indica se e' admin **/
    private final boolean isAdmin = false;

    /** Validatore per il dipendente */
    private static final Validator<Dipendente> validator = ValidatorBuilder.<Dipendente>of()
        .constraint(Dipendente::getUsername, "username",
            c -> c.notBlank()             // non puo' essere: nullo, vuoto, contenere solo spazi
                .lessThanOrEqual(50)      // lunghezza massima 50 caratteri
        )
        .constraint(Dipendente::getPassword, "password",
            c -> c.notBlank()                 // non puo' essere: nullo, vuoto, contenere solo spazi
                .greaterThanOrEqual(12)       // deve avere almeno 12 caratteri
                .lessThanOrEqual(100)    // lunghezza massima 100 caratteri
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
    protected Dipendente(String nome, String cognome, String luogoNascita, LocalDate dataNascita, String nazionalita,
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
     * Costruttore usato dalla libreria jackson
     * per generare oggetti a partire da JSON.
     */
    public Dipendente(){
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
     * Imposta nuovo username.
     * @param username Nuovo username
     */
    protected void setUsername(String username){
        if (username == null)
            throw new IllegalArgumentException("username non puo' essere nullo");
        this.username = username;
    }

    /**
     * Restituisce password del dipendente
     * @return Password del dipendente
     */
    protected String getPassword(){
        return this.password;
    }

    /**
     * Imposta nuova password.
     * @param password Nuova password
     */
    protected void setPassword(String password){
        if (password == null)
            throw new IllegalArgumentException("password non puo' essere nullo");
        this.password = password;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore dell'oggetto.
     * @return Violazioni nelle proprieta' oggetto
     */
    public ConstraintViolations validate(){
        ConstraintViolations violations = Dipendente.validator.validate(this);
        violations.addAll(super.validate());  // validator Persona
        return violations;
    }

    /**
     * Restituisce falso per gli oggetti Dipendente
     * @return false
     */
    public boolean isAdmin(){
        return false;
    }

    /**
     * Restituisce vero per gli oggetti Dipendente e Admin.
     * @return true
     */
    public boolean isDipendente(){
        return true;
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
        if (!super.equals(o)) return false;
        Dipendente that = (Dipendente) o;
        return username.equals(that.username) && password.equals(that.password);
    }

    /**
     * Restituisce hash
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password);
    }

    /**
     * Restituisce in formato leggibile le informazioni del dipendente
     * @return Stringa con informazioni dipendente
     */
    @Override
    public String toString() {
        return "Dipendente{" +
            "nome='" + getNome() + '\'' +
            ", cognome='" + getCognome() + '\'' +
            ", luogoNascita='" + getLuogoNascita() + '\'' +
            ", dataNascita=" + getDataNascita() +
            ", nazionalita='" + getNazionalita() + '\'' +
            ", indirizzoEmail='" + getIndirizzoEmail() + '\'' +
            ", numeroTelefono='" + getNumeroTelefono() + '\'' +
            ", username='" + username + '\'' +
            ", password='****'" +
            '}';
    }
}
