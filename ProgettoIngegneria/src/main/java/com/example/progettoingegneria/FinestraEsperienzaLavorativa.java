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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class FinestraEsperienzaLavorativa extends Application{
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraEsperienzaLavorativa.fxml"));

        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        TextField nome= (TextField) loader.getNamespace().get("inputNome");
        TextField ubicazione= (TextField) loader.getNamespace().get("inputUbicazione");
        TextField mansioni= (TextField) loader.getNamespace().get("inputMansioni");
        TextField giornoInizio= (TextField) loader.getNamespace().get("giornoInizio");
        TextField meseInizio= (TextField) loader.getNamespace().get("meseInizio");
        TextField annoInizio= (TextField) loader.getNamespace().get("annoInizio");
        TextField giornoFine= (TextField) loader.getNamespace().get("giornoFine");
        TextField meseFine= (TextField) loader.getNamespace().get("meseFine");
        TextField annoFine= (TextField) loader.getNamespace().get("annoFine");
        TextField retribuzione= (TextField) loader.getNamespace().get("inputRetribuzione");

        Button aggiungi= (Button) loader.getNamespace().get("AggiungiEsperienza");
        aggiungi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate inizioPeriodoLavorativo=LocalDate.of(Integer.parseInt(annoInizio.getText()),Integer.parseInt(meseInizio.getText()),Integer.parseInt(giornoInizio.getText()));
                LocalDate finePeriodoLavorativo=LocalDate.of(Integer.parseInt(annoFine.getText()),Integer.parseInt(meseFine.getText()),Integer.parseInt(giornoFine.getText()));
                LocalDate adesso = LocalDate.now();

                if(adesso.isAfter(LocalDate.of(Integer.parseInt(annoFine.getText())+5,Integer.parseInt(meseFine.getText()),Integer.parseInt(giornoFine.getText()))) || adesso.isAfter(LocalDate.of(Integer.parseInt(annoInizio.getText())+5,Integer.parseInt(meseInizio.getText()),Integer.parseInt(giornoInizio.getText()))))
                    JOptionPane.showMessageDialog(null, "L'ESPERIENZA NON DEVE ESSERE ANTECEDENT£E A 5 ANNI FA", "ERRORE", JOptionPane.ERROR_MESSAGE);
                else{
                    EsperienzaLavorativa esperienza=EsperienzaLavorativa.of(inizioPeriodoLavorativo,finePeriodoLavorativo,nome.getText(), Arrays.asList(mansioni.getText().split(",")),ubicazione.getText(), Integer.parseInt(retribuzione.getText()));
                    FinestraLavoratore.getEsperienze().add(esperienza);
                    stage.close();
                }
            }
        });

        stage.setScene(scene);
        stage.setTitle("Aggiungi Esperienza");
        stage.setResizable(false);
        stage.show();
    }
}
