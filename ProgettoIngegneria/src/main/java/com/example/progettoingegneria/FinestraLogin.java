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
import java.net.URISyntaxException;

public class FinestraLogin extends Application{
    public void start(Stage stage) {
        Scene scene;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLogin.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        //prendo le credenziali
        TextField nomeUtente=(TextField) loader.getNamespace().get("inputUtente");
        PasswordField password=(PasswordField) loader.getNamespace().get("inputPassword");
        Button accedi= (Button) loader.getNamespace().get("accedi");
        accedi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    ManagementSystem ms = ManagementSystem.getInstance();
                    ms.login(nomeUtente.getText(),password.getText());
                    if(ms.getLoggedInUser()==null)
                        new FinestraErrore().start(new Stage());
                    else{
                        new FinestraRicerca().start(new Stage());
                        stage.close();
                    }
                }
                catch (IOException e){System.out.println(e);}
                catch (URISyntaxException e){System.out.println(e);}
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
