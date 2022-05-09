package com.example.progettoingegneria;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;


public class Main {

    public static void main(String[] args) throws JsonProcessingException{

        System.out.println("OGGETTI CREATI VIA CODICE:\n");
        Dipendente d = Dipendente.of(
            "nome",
            "cognome",
            "luogo nascita",
            LocalDate.of(2000, 4, 28),
            "italiana",
            "nome@example.com",
            "045045045",
            "ncognome",
            "secret",
            "5367654"
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
            List.of(Lingua.ITALIANO, Lingua.INGLESE),
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
            )),
            "ABCDEFGHILMNO"
        );
        System.out.println("Lavoratore valido? " + l.validate());
        System.out.println("Lavoratore: " + l.asJSON());

        Admin admin = Admin.of(
            "nome",
            "cognome",
            "luogo nascita",
            LocalDate.of(2000, 4, 28),
            "italiana",
            "nome@example.com",
            "045045045",
            "ncognome",
            "secret",
            "y546hnj"
        );
        System.out.println("Admin valido? " + admin.validate());
        System.out.println("Admin: " + admin.asJSON());
        System.out.println("Admin e' admin? " + admin.isAdmin());

        System.out.println("\nOGGETTI CREATI A PARTIRE DA JSON:\n");
        ObjectMapper objectMapper = JsonMapper.builder().enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER).build();
        objectMapper.findAndRegisterModules();
        String mcurley_dipendente_json = "{\"nome\": \"Michelle\", \"cognome\": \"Curley\", \"luogoNascita\": \"665 Poplar Chase Lane\", \"dataNascita\": \"1945-03-20\", \"nazionalita\": \"americana\", \"indirizzoEmail\": \"mcurley@example.com\", \"numeroTelefono\": \"678-623-0532\", \"tipo\": \"dipendente\", \"username\": \"mcurley\", \"password\": \"DL//9=(cd&={xX\\.\", \"codiceFiscale\":\"345213\"}";
        Dipendente mcurley_d = objectMapper.readValue(mcurley_dipendente_json, Dipendente.class);
        System.out.println("Dipendente valido? " + mcurley_d.validate());
        System.out.println("Dipendente: " + mcurley_d.asJSON());

        String vgoulette_l_json = "{\"nome\":\"Vincent\",\"cognome\":\"Goulette\",\"luogoNascita\":\"652 Keyser Ridge Road\",\"dataNascita\":\"1978-08-14\",\"nazionalita\":\"francese\",\"indirizzoEmail\":\"vgoulette@example.com\",\"numeroTelefono\":\"336-510-8563\",\"tipo\":\"lavoratore\",\"indirizzoResidenza\":\"4776 Bastin Drive\",\"esperienzeLavorative\":[{\"inizioPeriodoLavorativo\":\"2021-03-05\",\"finePeriodoLavorativo\":\"2021-06-05\",\"nomeAzienda\":\"Museum Company\",\"mansioniSvolte\":[\"guardia\"],\"luogoLavoro\":\"4686 Hall Valley Drive\",\"retribuzioneLordaGiornaliera\":120},{\"inizioPeriodoLavorativo\":\"2021-07-15\",\"finePeriodoLavorativo\":\"2021-09-25\",\"nomeAzienda\":\"Infinite Wealth Planners\",\"mansioniSvolte\":[\"guardia\"],\"luogoLavoro\":\"3862 Earnhardt Drive\",\"retribuzioneLordaGiornaliera\":155}],\"lingueParlate\":[\"ITALIANO\",\"INGLESE\"],\"patenti\":[\"A\",\"B\"],\"automunito\":true,\"periodiDisponibilita\":[{\"inizioPeriodoDisponibilita\":\"2022-05-10\",\"finePeriodoDisponibilita\":\"2022-05-25\",\"comune\":\"Via MoltoProbabilmenteNonEsistente 5 Verona\"}],\"recapitiUrgenze\":[{\"nome\":\"nome\",\"cognome\":\"cognome\",\"numeroTelefono\":\"098345098\",\"indirizzoEmail\":\"nome@example.com\"}], \"codiceFiscale\":\"1234567\"}";
        Lavoratore vgoulette_l = objectMapper.readValue(vgoulette_l_json, Lavoratore.class);
        System.out.println("Lavoratore valido? " + vgoulette_l.validate());
        System.out.println("Lavoratore: " + vgoulette_l.asJSON());

        System.out.println("\nMANAGEMENT SYSTEM:\n");

        ManagementSystem ms = null;
        try {
            ms = ManagementSystem.getInstance();
            ms.login("mario", "secret");
            System.out.println("Aggiungi dipendente");
            ms.addDipendente(d);
            System.out.println("Aggiungi lavoratore");
            ms.addLavoratore(vgoulette_l);
        }
        catch (IOException e){
            System.out.println(e);
        }
        catch (URISyntaxException e){
            System.out.println(e);
        }

        if (ms != null)
            System.out.println(ms);
    }
}
