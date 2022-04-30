package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class FinestraRicerca extends Application {
    public void start(Stage stage) {
        Scene scene;
        Button aggiungiLavoratore;
        Button aggiungiMansione;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraRicerca.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        aggiungiLavoratore= (Button) loader.getNamespace().get("aggiungiLavoratore");
        aggiungiLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraLavoratore().start(new Stage());
            }
        });

        aggiungiMansione= (Button) loader.getNamespace().get("aggiungiMansione");
        aggiungiMansione.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraEsperienzaLavorativa().start(new Stage());
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
