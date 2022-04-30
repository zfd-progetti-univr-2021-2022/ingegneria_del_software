package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class FinestraDipendente extends Application {
    public void start(Stage stage) {
        Scene scene;
        Button registraDipendente;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraDipendente.fxml"));

        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        registraDipendente= (Button) loader.getNamespace().get("registraDipendente");
        registraDipendente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Evviva");
                stage.close();
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
