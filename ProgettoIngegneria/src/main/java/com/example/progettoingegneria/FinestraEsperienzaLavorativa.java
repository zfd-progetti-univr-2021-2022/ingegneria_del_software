package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FinestraEsperienzaLavorativa extends Application{
    Collection<EsperienzaLavorativa> esperienze=new ArrayList<EsperienzaLavorativa>();
    Lavoratore lavoratore=null;
    EsperienzaLavorativa esperienza=null;
    boolean modifica=false;
    ManagementSystem ms;
    TextField nome, ubicazione, mansioni, giornoInizio, meseInizio, annoInizio, giornoFine, meseFine, annoFine, retribuzione;

    public FinestraEsperienzaLavorativa(Lavoratore l, EsperienzaLavorativa e){super();lavoratore=l;esperienza=e;modifica=true;}

    public FinestraEsperienzaLavorativa(Collection<EsperienzaLavorativa> c){super();esperienze=c;}

    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraEsperienzaLavorativa.fxml"));

        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        nome= (TextField) loader.getNamespace().get("inputNome");
        ubicazione= (TextField) loader.getNamespace().get("inputUbicazione");
        mansioni= (TextField) loader.getNamespace().get("inputMansioni");
        giornoInizio= (TextField) loader.getNamespace().get("giornoInizio");
        meseInizio= (TextField) loader.getNamespace().get("meseInizio");
        annoInizio= (TextField) loader.getNamespace().get("annoInizio");
        giornoFine= (TextField) loader.getNamespace().get("giornoFine");
        meseFine= (TextField) loader.getNamespace().get("meseFine");
        annoFine= (TextField) loader.getNamespace().get("annoFine");
        retribuzione= (TextField) loader.getNamespace().get("inputRetribuzione");

        try {ms = ManagementSystem.getInstance();}
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}

        if(modifica)
            inizializzaEsperienza();

        Button aggiungi= (Button) loader.getNamespace().get("AggiungiEsperienza");
        aggiungi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate inizioPeriodoLavorativo=LocalDate.of(Integer.parseInt(annoInizio.getText()),Integer.parseInt(meseInizio.getText()),Integer.parseInt(giornoInizio.getText()));
                LocalDate finePeriodoLavorativo=LocalDate.of(Integer.parseInt(annoFine.getText()),Integer.parseInt(meseFine.getText()),Integer.parseInt(giornoFine.getText()));
                EsperienzaLavorativa esp=EsperienzaLavorativa.of(inizioPeriodoLavorativo,finePeriodoLavorativo,nome.getText(), Arrays.asList(mansioni.getText().split(",")),ubicazione.getText(), Double.parseDouble(retribuzione.getText().replaceAll(" ","")));

                if(modifica==false)
                    esperienze.add(esp);
                else{
                    try{ms.modifyEsperienzaLavorativa(lavoratore.getCodiceFiscale(), esperienza.getId(), esp);}
                    catch(Exception e){System.out.println("Inserimento fallito");}
                }
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Aggiungi Esperienza");
        stage.setResizable(false);
        stage.show();
    }

    private void inizializzaEsperienza() {

        nome.setText(esperienza.getNomeAzienda());
        ubicazione.setText(esperienza.getLuogoLavoro());

        String mans="";
        for(String s:esperienza.getMansioniSvolte())
            mans=mans+s+",";
        mansioni.setText(mans.substring(0,mans.length()-1));

        giornoInizio.setText(""+esperienza.getInizioPeriodoLavorativo().getDayOfMonth());
        meseInizio.setText(""+esperienza.getInizioPeriodoLavorativo().getMonth().getValue());
        annoInizio.setText(""+esperienza.getInizioPeriodoLavorativo().getYear());

        giornoFine.setText(""+esperienza.getFinePeriodoLavorativo().getDayOfMonth());
        meseFine.setText(""+esperienza.getFinePeriodoLavorativo().getMonth().getValue());
        annoFine.setText(""+esperienza.getFinePeriodoLavorativo().getYear());

        retribuzione.setText(""+esperienza.getRetribuzioneLordaGiornaliera());
    }
}
