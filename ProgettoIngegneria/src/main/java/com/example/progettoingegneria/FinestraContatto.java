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


public class FinestraContatto extends Application{

    public void start(Stage stage) {
        Scene scene;
        stage.setTitle("Aggiungi Contatto");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraContatto.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        TextField nome= (TextField) loader.getNamespace().get("inputNome");
        TextField cognome= (TextField) loader.getNamespace().get("inputCognome");
        TextField mail= (TextField) loader.getNamespace().get("inputMail");
        TextField recapito= (TextField) loader.getNamespace().get("inputRecapito");

        Button aggiungi= (Button) loader.getNamespace().get("AggiungiContatto");
        aggiungi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                RecapitoUrgenza contatto = RecapitoUrgenza.of(nome.getText(),cognome.getText(), recapito.getText(), mail.getText());
                if(contatto.validate().size()==0){
                    FinestraLavoratore.getRecapitiUrgenze().add(contatto);
                    stage.close();
                }
                else
                    JOptionPane.showMessageDialog(null, contatto.validate().toString(), "ERRORE", JOptionPane.ERROR_MESSAGE);
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
