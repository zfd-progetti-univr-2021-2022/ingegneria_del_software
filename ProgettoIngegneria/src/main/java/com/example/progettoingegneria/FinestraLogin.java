package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class FinestraLogin extends Application{
    public void start(Stage stage) {
        Scene scene;
        Button accedi;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLogin.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        accedi= (Button) loader.getNamespace().get("accedi");
        accedi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //prendo le credenziali
                TextField nomeUtente=(TextField) loader.getNamespace().get("inputUtente");
                PasswordField password=(PasswordField) loader.getNamespace().get("inputPassword");
                //se inserisco le credenziali dell'amministratore apro la finestra per aggiungere i dipendenti
                if(nomeUtente.getText().equals("mario") && password.getText().equals("secret")){
                    new FinestraDipendente().start(new Stage());
                    stage.close();
                }
                else{
                    new FinestraRicerca().start(new Stage());
                    stage.close();
                }
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
