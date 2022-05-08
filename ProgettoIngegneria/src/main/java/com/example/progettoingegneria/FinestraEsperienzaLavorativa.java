package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class FinestraEsperienzaLavorativa extends Application{
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraEsperienzaLavorativa.fxml"));

        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        Button aggiungi= (Button) loader.getNamespace().get("AggiungiEsperienza");
        aggiungi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.setTitle("Aggiungi Dipendente");
        stage.setResizable(false);
        stage.show();
    }
}
