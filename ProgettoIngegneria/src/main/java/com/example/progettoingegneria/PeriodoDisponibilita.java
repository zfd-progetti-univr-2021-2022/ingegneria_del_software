package com.example.progettoingegneria;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

class PeriodoDisponibilita {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inizioPeriodoDisponibilita;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate finePeriodoDisponibilita;

    private String comune;

    private PeriodoDisponibilita(LocalDate inizioPeriodoDisponibilita, LocalDate finePeriodoDisponibilita, String comune){
        this.inizioPeriodoDisponibilita = inizioPeriodoDisponibilita;
        this.finePeriodoDisponibilita = finePeriodoDisponibilita;
        this.comune = comune;
    }

    public static PeriodoDisponibilita of(LocalDate inizioPeriodoDisponibilita, LocalDate finePeriodoDisponibilita, String comune){
        return new PeriodoDisponibilita(
            inizioPeriodoDisponibilita, finePeriodoDisponibilita, comune
        );
    }
}
