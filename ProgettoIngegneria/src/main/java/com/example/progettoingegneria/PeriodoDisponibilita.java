/**
 * Definisce classe che rappresenta un periodo di disponibilita' di un lavoratore.
 */
package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta un periodo di disponibilita' di un lavoratore.
 *
 * Per creare un oggetto utilizzare il factory method of().
 */
public class PeriodoDisponibilita {

    /** Data inizio periodo di disponibilita' del lavoratore */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inizioPeriodoDisponibilita;

    /** Data fine periodo di disponibilita' del lavoratore */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate finePeriodoDisponibilita;

    /** Comune per cui il lavoratore e' disponibile */
    private Comune comune;

    /** Oggetto per validare i dati di un periodo di disponibilita' */
    @JsonIgnore
    private final Validator<PeriodoDisponibilita> validator = ValidatorBuilder.<PeriodoDisponibilita>of()
        .constraint(PeriodoDisponibilita::getInizioPeriodoDisponibilita, "inizio periodo",
            c -> c.notNull()                                      // non puo': essere null
                .beforeOrEqual(this::getFinePeriodoDisponibilita) // deve venire prima della data di fine
        )
        .constraint(PeriodoDisponibilita::getFinePeriodoDisponibilita, "fine periodo",
            c -> c.notNull()                                       // non puo': essere null
                .afterOrEqual(this::getInizioPeriodoDisponibilita) // deve venire dopo della data di inizio
        )
        .build();

    /**
     * Crea oggetto che rappresenta un periodo di disponibilita' del lavoratore
     * @param inizioPeriodoDisponibilita Data inizio periodo di disponibilita'
     * @param finePeriodoDisponibilita Data fine periodo di disponibilita'
     * @param comune Comune per cui il lavoratore e' disponibile
     */
    private PeriodoDisponibilita(LocalDate inizioPeriodoDisponibilita, LocalDate finePeriodoDisponibilita, Comune comune){

        if (inizioPeriodoDisponibilita == null || finePeriodoDisponibilita == null || comune == null){
            throw new IllegalArgumentException("I parametri non possono essere nulli");
        }
        this.inizioPeriodoDisponibilita = inizioPeriodoDisponibilita;
        this.finePeriodoDisponibilita = finePeriodoDisponibilita;
        this.comune = comune;
    }

    /**
     * Costruttore usato da jackson per
     * creare oggetti a partire da JSON
     */
    public PeriodoDisponibilita(){}

    /**
     * Factory method che restituisce un oggetto che rappresenta un periodo di disponibilita' del lavoratore
     * @param inizioPeriodoDisponibilita Data inizio periodo di disponibilita'
     * @param finePeriodoDisponibilita Data fine periodo di disponibilita'
     * @param comune Comune per cui il lavoratore e' disponibile
     * @return Oggetto che rappresenta il periodo di disponibilita' del lavoratore
     */
    public static PeriodoDisponibilita of(LocalDate inizioPeriodoDisponibilita, LocalDate finePeriodoDisponibilita, Comune comune){
        return new PeriodoDisponibilita(
            inizioPeriodoDisponibilita, finePeriodoDisponibilita, comune
        );
    }

    /**
     * Restituisce data di inizio del periodo di disponibilita' del lavoratore.
     * @return Data di inizio del periodo di disponibilita'
     */
    protected LocalDate getInizioPeriodoDisponibilita(){
        return this.inizioPeriodoDisponibilita;
    }

    /**
     * Imposta data inizio periodo disponibilita'
     * @param inizioPeriodoDisponibilita data inizio periodo disponibilita'
     */
    protected void setInizioPeriodoDisponibilita(LocalDate inizioPeriodoDisponibilita){
        if (inizioPeriodoDisponibilita == null)
            throw new IllegalArgumentException("inizioPeriodoDisponibilita non puo' essere null");
        this.inizioPeriodoDisponibilita = inizioPeriodoDisponibilita;
    }

    /**
     * Restituisce data di fine del periodo di disponibilita' del lavoratore.
     * @return Data di fine del periodo di disponibilita'
     */
    protected LocalDate getFinePeriodoDisponibilita(){
        return this.finePeriodoDisponibilita;
    }

    /**
     * Imposta data fine periodo disponibilita'
     * @param finePeriodoDisponibilita fine periodo disponibilita'
     */
    protected  void setFinePeriodoDisponibilita(LocalDate finePeriodoDisponibilita){
        if (finePeriodoDisponibilita == null)
            throw new IllegalArgumentException("finePeriodoDisponibilita non puo' essere null");
        this.finePeriodoDisponibilita = finePeriodoDisponibilita;
    }

    /**
     * Restituisce il comune per cui il lavoratore e' disponibile
     * @return Comune per cui il lavoratore e' disponibile
     */
    public Comune getComune(){
        return this.comune;
    }

    /**
     * Imposta comune per cui il lavoratore e' disponibile
     * @param comune Comune per cui il lavoratore e' disponibile
     */
    protected void setComune(Comune comune){
        if (comune == null)
            throw new IllegalArgumentException("comune non puo' essere null");
        this.comune = comune;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore del periodo di disponibilita'.
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
        PeriodoDisponibilita that = (PeriodoDisponibilita) o;
        return inizioPeriodoDisponibilita.equals(that.inizioPeriodoDisponibilita) && finePeriodoDisponibilita.equals(that.finePeriodoDisponibilita) && comune.equals(that.comune);
    }

    /**
     * Restituisce hash
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(inizioPeriodoDisponibilita, finePeriodoDisponibilita, comune);
    }

    @Override
    public String toString(){
        return inizioPeriodoDisponibilita.getDayOfMonth()+"/"+ inizioPeriodoDisponibilita.getMonth().getValue()+"/"+ inizioPeriodoDisponibilita.getYear()+"-"+finePeriodoDisponibilita.getDayOfMonth()+"/"+ finePeriodoDisponibilita.getMonth().getValue()+"/"+ finePeriodoDisponibilita.getYear();
    }
}
