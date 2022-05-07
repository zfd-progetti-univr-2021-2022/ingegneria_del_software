/**
 * Definisce classe che rappresenta una esperienza lavorativa.
 */

package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.constraint.base.ContainerConstraintBase;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.Collection;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta una esperienza lavorativa.
 *
 * Per creare un oggetto usare il factory method of().
 */
public class EsperienzaLavorativa {

    /** Inizio periodo lavorativo */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inizioPeriodoLavorativo;

    /** Fine periodo lavorativo */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate finePeriodoLavorativo;

    /** Nome azienda */
    private String nomeAzienda;
    /** Mansioni svolte */
    private Collection<String> mansioniSvolte;
    /** Luogo di lavoro */
    private String luogoLavoro;
    /** Retribuzione lorda giornaliera */
    private int retribuzioneLordaGiornaliera;

    /** Validatore per l'esperienza lavorativa */
    @JsonIgnore
    private final Validator<EsperienzaLavorativa> validator = ValidatorBuilder.<EsperienzaLavorativa>of()
        .constraint(EsperienzaLavorativa::getInizioPeriodoLavorativo, "inizio periodo",
            c -> c.notNull()                                      // non puo': essere null
                .beforeOrEqual(this::getFinePeriodoLavorativo)    // deve venire prima della data di fine
        )
        .constraint(EsperienzaLavorativa::getFinePeriodoLavorativo, "fine periodo",
            c -> c.notNull()                                      // non puo': essere null
                .afterOrEqual(this::getInizioPeriodoLavorativo)   // deve venire dopo della data di inizio
        )
        .constraint(EsperienzaLavorativa::getNomeAzienda, "nome azienda",
            c -> c.notBlank()          // non puo': essere null, vuota, contenere solo spazi
                .lessThanOrEqual(100)  // lunghezza massima 100
        )
        .constraint(EsperienzaLavorativa::getMansioniSvolte, "mansioni svolte",
            ContainerConstraintBase::notEmpty  // non puo' essere: nulla o vuota
        )
        .forEachIfPresent(
            EsperienzaLavorativa::getMansioniSvolte, "mansioni",
            m -> m._string(String::toString, "mansione",
                c -> c.notBlank()              // non puo': essere null, vuota, contenere solo spazi
                    .lessThanOrEqual(50)  // lunghezza massima 50 caratteri
            )
        )
        .constraint(EsperienzaLavorativa::getLuogoLavoro, "luogo lavoro",
            c -> c.notBlank()          // non puo': essere null, vuota, contenere solo spazi
                .lessThanOrEqual(255)  // lunghezza massima 255
        )
        .constraint(EsperienzaLavorativa::getRetribuzioneLordaGiornaliera, "retribuzione",
            c -> c.greaterThanOrEqual(0)  // deve essere almeno 0
        )
        .build();

    /**
     * Costruttore che crea un oggetto che rappresenta una esperienza lavorativa.
     *
     * @param inizioPeriodoLavorativo Inizio periodo lavorativo
     * @param finePeriodoLavorativo Fine periodo lavorativo
     * @param nomeAzienda Nome azienda
     * @param mansioniSvolte Collezione mansioni svolte
     * @param luogoLavoro Luogo di lavoro
     * @param retribuzioneLordaGiornaliera Retribuzione lorda giornaliera
     */
    private EsperienzaLavorativa(LocalDate inizioPeriodoLavorativo, LocalDate finePeriodoLavorativo,
                                String nomeAzienda, Collection<String> mansioniSvolte,
                                String luogoLavoro, int retribuzioneLordaGiornaliera){

        if (inizioPeriodoLavorativo == null || finePeriodoLavorativo == null
            || nomeAzienda == null || mansioniSvolte == null || luogoLavoro == null) {
            throw new IllegalArgumentException("I parametri non possono essere nulli");
        }
        this.inizioPeriodoLavorativo = inizioPeriodoLavorativo;
        this.finePeriodoLavorativo = finePeriodoLavorativo;
        this.nomeAzienda = nomeAzienda;
        this.mansioniSvolte = mansioniSvolte;
        this.luogoLavoro = luogoLavoro;
        this.retribuzioneLordaGiornaliera = retribuzioneLordaGiornaliera;
    }

    /**
     * Costruttore usato da jackson
     * per creare oggetti a partire da JSON.
     */
    public EsperienzaLavorativa(){}

    /**
     * Factory method che restituisce una esperienza lavorativa.
     *
     * @param inizioPeriodoLavorativo Inizio periodo lavorativo
     * @param finePeriodoLavorativo Fine periodo lavorativo
     * @param nomeAzienda Nome azienda
     * @param mansioniSvolte Collezione mansioni svolte
     * @param luogoLavoro Luogo di lavoro
     * @param retribuzioneLordaGiornaliera Retribuzione lorda giornaliera
     * @return Oggetto che rappresenta una esperienza lavorativa
     */
    public static EsperienzaLavorativa of(LocalDate inizioPeriodoLavorativo, LocalDate finePeriodoLavorativo,
                                   String nomeAzienda, Collection<String> mansioniSvolte,
                                   String luogoLavoro, int retribuzioneLordaGiornaliera){
        return new EsperienzaLavorativa(
            inizioPeriodoLavorativo,
            finePeriodoLavorativo,
            nomeAzienda,
            mansioniSvolte,
            luogoLavoro,
            retribuzioneLordaGiornaliera
        );
    }

    /**
     * Restituisce data inizio periodo lavorativo
     * @return Data Inizio periodo lavorativo
     */
    protected LocalDate getInizioPeriodoLavorativo(){
        return this.inizioPeriodoLavorativo;
    }

    /**
     * Imposta una data di inizio periodo lavorativo.
     * @param inizioPeriodoLavorativo Data di inizio periodo lavorativo
     */
    protected void setInizioPeriodoLavorativo(LocalDate inizioPeriodoLavorativo){
        if (inizioPeriodoLavorativo == null)
            throw new IllegalArgumentException("inizioPeriodoLavorativo non puo' essere null");
        this.inizioPeriodoLavorativo = inizioPeriodoLavorativo;
    }

    /**
     * Restituisce data fine periodo lavorativo
     * @return data Fine periodo lavorativo
     */
    protected LocalDate getFinePeriodoLavorativo(){
        return this.finePeriodoLavorativo;
    }

    /**
     * Imposta una data di fine periodo lavorativo.
     * @param finePeriodoLavorativo Data di fine periodo lavorativo.
     */
    protected void setFinePeriodoLavorativo(LocalDate finePeriodoLavorativo){
        if (finePeriodoLavorativo == null)
            throw new IllegalArgumentException("finePeriodoLavorativo non puo' essere null");
        this.finePeriodoLavorativo = finePeriodoLavorativo;
    }

    /**
     * Restituisce nome azienda.
     * @return Nome azienda
     */
    protected String getNomeAzienda(){
        return this.nomeAzienda;
    }

    /**
     * Imposta nome azienda.
     * @param nomeAzienda nome azienda
     */
    protected void setNomeAzienda(String nomeAzienda){
        if (nomeAzienda == null)
            throw new IllegalArgumentException("nomeAzienda non puo' essere null");
        this.nomeAzienda = nomeAzienda;
    }

    /**
     * Restituisce mansioni svolte.
     * @return Mansioni svolte
     */
    protected Collection<String> getMansioniSvolte(){
        return this.mansioniSvolte;
    }

    /**
     * Imposta mansioni svolte.
     * @param mansioniSvolte Mansioni svolte
     */
    protected void setMansioniSvolte(Collection<String> mansioniSvolte){
        if (mansioniSvolte == null)
            throw new IllegalArgumentException("mansioniSvolte non puo' essere null");
        this.mansioniSvolte = mansioniSvolte;
    }

    /**
     * Aggiunge una mansione tra le mansioni svolte.
     * @param mansioneSvolta mansione svolta da aggiungere
     * @return true se mansioneSvolta e' stata aggiunta
     */
    protected boolean addMansioneSvolta(String mansioneSvolta){
        if (mansioneSvolta == null)
            throw new IllegalArgumentException("mansioneSvolta non puo' essere null");

        if (!this.mansioniSvolte.contains(mansioneSvolta)) {
            return this.mansioniSvolte.add(mansioneSvolta);
        }
        return false;
    }

    /**
     * Rimuove una mansione tra le mansioni svolte.
     * @param mansioneSvolta mansione svolta da rimuovere
     * @return true se mansioneSvolta e' stato rimosso
     */
    protected boolean removeMansioneSvolta(String mansioneSvolta){
        if (mansioneSvolta == null)
            throw new IllegalArgumentException("mansioneSvolta non puo' essere null");

        return this.mansioniSvolte.remove(mansioneSvolta);
    }

    /**
     * Restituisce luogo di lavoro.
     * @return Luogo di lavoro
     */
    protected String getLuogoLavoro(){
        return this.luogoLavoro;
    }

    /**
     * Imposta luogo di lavoro.
     * @param luogoLavoro Luogo di lavoro
     */
    protected void setLuogoLavoro(String luogoLavoro){
        if (luogoLavoro == null)
            throw new IllegalArgumentException("luogoLavoro non puo' essere null");
        this.luogoLavoro = luogoLavoro;
    }

    /**
     * Restituisce retribuzione lorda giornaliera.
     * @return Retribuzione lorda giornaliera
     */
    protected int getRetribuzioneLordaGiornaliera(){
        return this.retribuzioneLordaGiornaliera;
    }

    /**
     * Imposta retribuzione lorda giornaliera.
     * @param retribuzioneLordaGiornaliera retribuzione lorda giornaliera
     */
    protected void setRetribuzioneLordaGiornaliera(int retribuzioneLordaGiornaliera){
        this.retribuzioneLordaGiornaliera = retribuzioneLordaGiornaliera;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore delle esperienze lavorative.
     * @return Violazioni nelle proprieta' oggetto
     */
    public List<String> validate(){
        List<String> violationsMessages = new ArrayList<>();
        ConstraintViolations violations = this.validator.validate(this);
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
        EsperienzaLavorativa that = (EsperienzaLavorativa) o;
        return retribuzioneLordaGiornaliera == that.retribuzioneLordaGiornaliera && inizioPeriodoLavorativo.equals(that.inizioPeriodoLavorativo) && finePeriodoLavorativo.equals(that.finePeriodoLavorativo) && nomeAzienda.equals(that.nomeAzienda) && mansioniSvolte.equals(that.mansioniSvolte) && luogoLavoro.equals(that.luogoLavoro);
    }

    /**
     * Restituisce hash
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(inizioPeriodoLavorativo, finePeriodoLavorativo, nomeAzienda, mansioniSvolte, luogoLavoro, retribuzioneLordaGiornaliera);
    }
}
