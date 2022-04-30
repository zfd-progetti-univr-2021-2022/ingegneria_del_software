package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

public class FinestraLavoratore extends Application{
    public void start(Stage stage) {
        Scene scene;
        Button aggiungiContatto;
        Button registraLavoratore;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLavoratore.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        aggiungiContatto= (Button) loader.getNamespace().get("AggiungiContattoLavoratore");
        aggiungiContatto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraContatto().start(new Stage());
            }
        });

        registraLavoratore= (Button) loader.getNamespace().get("registraLavoratore");
        registraLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
