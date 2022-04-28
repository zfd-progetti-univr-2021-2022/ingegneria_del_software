package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;


public class FinestraContatto extends Application{

    public void start(Stage stage) {
        Scene scene;
        Button aggiungi;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraContatto.fxml"));

        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        aggiungi= (Button) loader.getNamespace().get("AggiungiContatto");
        aggiungi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Evviva");
            }
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
