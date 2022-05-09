package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class FinestraRicerca extends Application {
    public void start(Stage stage) {
        Scene scene;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraRicerca.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        Button aggiungiLavoratore= (Button) loader.getNamespace().get("aggiungiLavoratore");
        aggiungiLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraLavoratore().start(new Stage());
            }
        });

        Button aggiungiDipendente= (Button) loader.getNamespace().get("aggiungiDipendente");

        try {
            ManagementSystem ms = ManagementSystem.getInstance();
            //se non sei amministratore non ti do la possibilità di aggiungere dipendenti
            if(!(ms.getLoggedInUser().isAdmin()))
                aggiungiDipendente.setVisible(false);
        }
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}

        aggiungiDipendente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere dipendenti
                new FinestraDipendente().start(new Stage());
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
