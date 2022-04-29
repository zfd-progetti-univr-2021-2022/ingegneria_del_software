package com.example.progettoingegneria;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Collection;
import java.time.LocalDate;


class EsperienzaLavorativa {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inizioPeriodoLavorativo;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate finePeriodoLavorativo;

    private String nomeAzienda;
    private Collection<String> mansioniSvolte;
    private String luogoLavoro;
    private int retribuzioneLordaGiornaliera;

    private EsperienzaLavorativa(LocalDate inizioPeriodoLavorativo, LocalDate finePeriodoLavorativo,
                                String nomeAzienda, Collection<String> mansioniSvolte,
                                String luogoLavoro, int retribuzioneLordaGiornaliera){
        this.inizioPeriodoLavorativo = inizioPeriodoLavorativo;
        this.finePeriodoLavorativo = finePeriodoLavorativo;
        this.nomeAzienda = nomeAzienda;
        this.mansioniSvolte = mansioniSvolte;
        this.luogoLavoro = luogoLavoro;
        this.retribuzioneLordaGiornaliera = retribuzioneLordaGiornaliera;
    }

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

    protected LocalDate getInizioPeriodoLavorativo(){
        return this.inizioPeriodoLavorativo;
    }

    protected LocalDate getFinePeriodoLavorativo(){
        return this.finePeriodoLavorativo;
    }

    protected String getNomeAzienda(){
        return this.nomeAzienda;
    }

    protected Collection<String> getMansioniSvolte(){
        return this.mansioniSvolte;
    }

    protected String getLuogoLavoro(){
        return this.luogoLavoro;
    }

    protected int getRetribuzioneLordaGiornaliera(){
        return this.retribuzioneLordaGiornaliera;
    }

}
