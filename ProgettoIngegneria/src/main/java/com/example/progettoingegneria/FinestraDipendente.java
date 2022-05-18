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

public class FinestraDipendente extends Application {
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraDipendente.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        TextField nome= (TextField) loader.getNamespace().get("inputNome");
        TextField cognome= (TextField) loader.getNamespace().get("inputCognome");
        TextField codice= (TextField) loader.getNamespace().get("inputCodice");
        TextField luogoNascita= (TextField) loader.getNamespace().get("inputLuogoNascita");
        TextField giornoNascita= (TextField) loader.getNamespace().get("giornoNascita");
        TextField meseNascita= (TextField) loader.getNamespace().get("meseNascita");
        TextField annoNascita= (TextField) loader.getNamespace().get("annoNascita");
        TextField nazionalita= (TextField) loader.getNamespace().get("inputNazionalita");
        TextField mail= (TextField) loader.getNamespace().get("inputMail");
        TextField numero= (TextField) loader.getNamespace().get("inputRecapito");
        TextField utente= (TextField) loader.getNamespace().get("inputUtente");
        TextField password= (TextField) loader.getNamespace().get("inputPassword");

        Button registraDipendente= (Button) loader.getNamespace().get("registraDipendente");
        registraDipendente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LocalDate dataNascita=LocalDate.of(Integer.parseInt(annoNascita.getText()),Integer.parseInt(meseNascita.getText()),Integer.parseInt(giornoNascita.getText()));
               Dipendente d= Dipendente.of(nome.getText(),cognome.getText(),luogoNascita.getText(),dataNascita,nazionalita.getText(),
                        mail.getText(), numero.getText(), utente.getText(), password.getText(),codice.getText());
                try {
                    ManagementSystem ms = ManagementSystem.getInstance();
                    if(d.validate().size()==0){
                        ms.addDipendente(d);
                        stage.close();
                    }
                    else
                        JOptionPane.showMessageDialog(null, d.validate().toString(), "ERRORE", JOptionPane.ERROR_MESSAGE);
                }
                catch (IOException e){System.out.println(e);}
                catch (URISyntaxException e){System.out.println(e);}

            }
        });

        stage.setScene(scene);
        stage.setTitle("Aggiungi Dipendente");
        stage.setResizable(false);
        stage.show();
    }
}
