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

/**
 * Rappresenta un periodo di disponibilita' di un lavoratore.
 *
 * Per creare un oggetto utilizzare il factory method of().
 */
class PeriodoDisponibilita {

    /** Data inizio periodo di disponibilita' del lavoratore */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate inizioPeriodoDisponibilita;

    /** Data fine periodo di disponibilita' del lavoratore */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate finePeriodoDisponibilita;

    /** Comune per cui il lavoratore e' disponibile */
    private final String comune;

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
        .constraint(PeriodoDisponibilita::getComune, "comune",
            c -> c.notBlank()                                      // non puo': essere null, contenere solo spazi, essere vuota
                .lessThanOrEqual(255)                         // lunghezza massima 255 caratteri
        )
        .build();

    /**
     * Crea oggetto che rappresenta un periodo di disponibilita' del lavoratore
     * @param inizioPeriodoDisponibilita Data inizio periodo di disponibilita'
     * @param finePeriodoDisponibilita Data fine periodo di disponibilita'
     * @param comune Comune per cui il lavoratore e' disponibile
     */
    private PeriodoDisponibilita(LocalDate inizioPeriodoDisponibilita, LocalDate finePeriodoDisponibilita, String comune){

        if (inizioPeriodoDisponibilita == null || finePeriodoDisponibilita == null || comune == null){
            throw new IllegalArgumentException("I parametri non possono essere nulli");
        }
        this.inizioPeriodoDisponibilita = inizioPeriodoDisponibilita;
        this.finePeriodoDisponibilita = finePeriodoDisponibilita;
        this.comune = comune;
    }

    /**
     * Factory method che restituisce un oggetto che rappresenta un periodo di disponibilita' del lavoratore
     * @param inizioPeriodoDisponibilita Data inizio periodo di disponibilita'
     * @param finePeriodoDisponibilita Data fine periodo di disponibilita'
     * @param comune Comune per cui il lavoratore e' disponibile
     * @return Oggetto che rappresenta il periodo di disponibilita' del lavoratore
     */
    public static PeriodoDisponibilita of(LocalDate inizioPeriodoDisponibilita, LocalDate finePeriodoDisponibilita, String comune){
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
     * Restituisce data di fine del periodo di disponibilita' del lavoratore.
     * @return Data di fine del periodo di disponibilita'
     */
    protected LocalDate getFinePeriodoDisponibilita(){
        return this.finePeriodoDisponibilita;
    }

    /**
     * Restituisce il comune per cui il lavoratore e' disponibile
     * @return Comune per cui il lavoratore e' disponibile
     */
    protected String getComune(){
        return this.comune;
    }

    /**
     * Restituisce le violazioni rilevate dal validatore del periodo di disponibilita'.
     * @return Violazioni nelle proprieta' oggetto
     */
    public ConstraintViolations validate(){
        ConstraintViolations violations = this.validator.validate(this);
        return violations;
    }
}
