package com.example.progettoingegneria;

import am.ik.yavi.builder.ValidatorBuilder;
import am.ik.yavi.core.ConstraintViolations;
import am.ik.yavi.core.Validator;

import java.util.Collection;
import java.time.LocalDate;

public class Lavoratore extends Persona{

    private final String tipo = "lavoratore";

    private static final Validator<Lavoratore> validator = ValidatorBuilder.<Lavoratore>of()
        .constraint(Lavoratore::getIndirizzoResidenza, "indirizzoResidenza", c -> c.notNull())
        .constraint(Lavoratore::getEsperienzeLavorative, "esperienzeLavorative", c -> c.notNull())
        .build();

    private String indirizzoResidenza;
    private Collection<EsperienzaLavorativa> esperienzeLavorative;
    private Collection<String> lingueParlate;
    private Collection<Patente> patenti;
    private Boolean automunito;
    private Collection<PeriodoDisponibilita> periodiDisponibilita;
    private Collection<RecapitoUrgenza> recapitiUrgenze;


    private Lavoratore(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                         String nazionalita, String indirizzoEmail, String numeroTelefono,
                         String indirizzoResidenza, Collection<EsperienzaLavorativa> esperienzeLavorative,
                         Collection<String> lingueParlate, Collection<Patente> patenti,
                         Boolean automunito, Collection<PeriodoDisponibilita> periodiDisponibilita,
                         Collection<RecapitoUrgenza> recapitiUrgenze) {
        super(nome, cognome, luogoNascita, dataNascita, nazionalita, indirizzoEmail, numeroTelefono);

        this.indirizzoResidenza = indirizzoResidenza;
        this.esperienzeLavorative = esperienzeLavorative;
        this.lingueParlate = lingueParlate;
        this.patenti = patenti;
        this.automunito = automunito;
        this.periodiDisponibilita = periodiDisponibilita;
        this.recapitiUrgenze = recapitiUrgenze;
    }

    public static Lavoratore of(String nome, String cognome, String luogoNascita, LocalDate dataNascita,
                                String nazionalita, String indirizzoEmail, String numeroTelefono,
                                String indirizzoResidenza, Collection<EsperienzaLavorativa> esperienzeLavorative,
                                Collection<String> lingueParlate, Collection<Patente> patenti,
                                Boolean automunito, Collection<PeriodoDisponibilita> periodiDisponibilita,
                                Collection<RecapitoUrgenza> recapitiUrgenze) {
        return new Lavoratore(
            nome,
            cognome,
            luogoNascita,
            dataNascita,
            nazionalita,
            indirizzoEmail,
            numeroTelefono,
            indirizzoResidenza,
            esperienzeLavorative,
            lingueParlate,
            patenti,
            automunito,
            periodiDisponibilita,
            recapitiUrgenze
        );
    }

    protected String getIndirizzoResidenza(){
        return this.indirizzoResidenza;
    }

    protected Collection<EsperienzaLavorativa> getEsperienzeLavorative(){
        return this.esperienzeLavorative;
    }

    protected Collection<String> getLingueParlate(){
        return this.lingueParlate;
    }

    protected Collection<Patente> getPatenti(){
        return this.patenti;
    }

    protected Boolean getAutomunito(){
        return this.automunito;
    }

    protected Collection<PeriodoDisponibilita> getPeriodiDisponibilita(){
        return this.periodiDisponibilita;
    }

    protected Collection<RecapitoUrgenza> getRecapitiUrgenze(){
        return this.recapitiUrgenze;
    }

    @Override
    public ConstraintViolations validate(){
        ConstraintViolations violations = Lavoratore.validator.validate(this);
        return violations;
    }
}
