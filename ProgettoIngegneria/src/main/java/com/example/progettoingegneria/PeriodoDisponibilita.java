/**
 * Definisce classe che rappresenta un periodo di disponibilita' di un lavoratore.
 *
 * TODO: Aggiungere un validator e un metodo validate (?)
 */
package com.example.progettoingegneria;

import com.fasterxml.jackson.annotation.JsonFormat;

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
}
