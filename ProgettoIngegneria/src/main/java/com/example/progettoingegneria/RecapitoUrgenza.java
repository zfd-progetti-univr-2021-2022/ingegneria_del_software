package com.example.progettoingegneria;

class RecapitoUrgenza {
    private String nome;
    private String cognome;
    private String numeroTelefono;
    private String indirizzoEmail;

    private RecapitoUrgenza(String nome, String cognome, String numeroTelefono, String indirizzoEmail){
        this.nome = nome;
        this.cognome = cognome;
        this.numeroTelefono = numeroTelefono;
        this.indirizzoEmail = indirizzoEmail;
    }

    public static RecapitoUrgenza of(String nome, String cognome, String numeroTelefono, String indirizzoEmail){
        return new RecapitoUrgenza(
            nome,
            cognome,
            numeroTelefono,
            indirizzoEmail
        );
    }

    protected String getNome(){
        return this.nome;
    }

    protected String getCognome(){
        return this.cognome;
    }

    protected String getNumeroTelefono(){
        return this.numeroTelefono;
    }

    protected String getIndirizzoEmail(){
        return this.indirizzoEmail;
    }
}
