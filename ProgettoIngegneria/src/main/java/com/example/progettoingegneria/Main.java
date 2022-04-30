package com.example.progettoingegneria;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDate;
import java.util.List;


public class Main {

    public static void main(String[] args) throws JsonProcessingException{

        Dipendente d = Dipendente.of(
            "nome",
            "cognome",
            "luogo nascita",
            LocalDate.of(2000, 4, 28),
            "italiana",
            "nome@example.com",
            "045045045",
            "ncognome",
            "secret"
        );


        System.out.println("Dipendente valido? " + d.validate());

        String resultDipendente = d.asJSON();
        System.out.println("Dipendente: " + resultDipendente);

        Lavoratore l = Lavoratore.of(
            "nome",
            "cognome",
            "luogo nascita",
            LocalDate.of(2001, 4, 28),
            "americana",
            "nome@example.com",
            "045678910",
            "Via SicuramenteEsistente 4 Verona",
            List.of(
                EsperienzaLavorativa.of(
                    LocalDate.of(2021, 3, 5),
                    LocalDate.of(2021, 6, 5),
                    "nomeOriginale",
                    List.of("bagnino", "barista"),
                    "luogoProbabile",
                    100
                ),
                EsperienzaLavorativa.of(
                    LocalDate.of(2021, 7, 15),
                    LocalDate.of(2021, 9, 25),
                    "nomeOriginale2",
                    List.of("musicista"),
                    "luogoProbabile2",
                    85
                )
            ),
            List.of("italiano", "inglese"),
            List.of(Patente.A, Patente.B),
            true,
            List.of(PeriodoDisponibilita.of(
                LocalDate.of(2022, 5, 10),
                LocalDate.of(2022, 5, 25),
                "Via MoltoProbabilmenteNonEsistente 5 Verona"
            )),
            List.of(RecapitoUrgenza.of(
                "nome",
                "cognome",
                "098345098",
                "nome@example.com"
            ))
        );
        System.out.println("Lavoratore: " + l.asJSON());

    }
}
