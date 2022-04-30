/**
 * Definisce classe che rappresenta una esperienza lavorativa.
 *
 * TODO: Aggiungere validator e un metodo validate (?)
 */

package com.example.progettoingegneria;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.time.LocalDate;

/**
 * Rappresenta una esperienza lavorativa.
 *
 * Per creare un oggetto usare il factory method of().
 */
class EsperienzaLavorativa {

    /** Inizio periodo lavorativo */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate inizioPeriodoLavorativo;

    /** Fine periodo lavorativo */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate finePeriodoLavorativo;

    /** Nome azienda */
    private final String nomeAzienda;
    /** Mansioni svolte */
    private final Collection<String> mansioniSvolte;
    /** Luogo di lavoro */
    private final String luogoLavoro;
    /** Retribuzione lorda giornaliera */
    private final int retribuzioneLordaGiornaliera;

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
     * Restituisce inizio periodo lavorativo
     * @return Inizio periodo lavorativo
     */
    protected LocalDate getInizioPeriodoLavorativo(){
        return this.inizioPeriodoLavorativo;
    }

    /**
     * Restituisce fine periodo lavorativo
     * @return Fine periodo lavorativo
     */
    protected LocalDate getFinePeriodoLavorativo(){
        return this.finePeriodoLavorativo;
    }

    /**
     * Restituisce nome azienda.
     * @return Nome azienda
     */
    protected String getNomeAzienda(){
        return this.nomeAzienda;
    }

    /**
     * Restituisce mansioni svolte.
     * @return Mansioni svolte
     */
    protected Collection<String> getMansioniSvolte(){
        return this.mansioniSvolte;
    }

    /**
     * Restituisce luogo di lavoro.
     * @return Luogo di lavoro
     */
    protected String getLuogoLavoro(){
        return this.luogoLavoro;
    }

    /**
     * Restituisce retribuzione lorda giornaliera.
     * @return Retribuzione lorda giornaliera
     */
    protected int getRetribuzioneLordaGiornaliera(){
        return this.retribuzioneLordaGiornaliera;
    }
}
