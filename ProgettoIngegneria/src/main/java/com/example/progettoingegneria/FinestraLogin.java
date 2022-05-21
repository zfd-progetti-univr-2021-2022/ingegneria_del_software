package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class FinestraLogin extends Application{
    ManagementSystem ms;
    public void start(Stage stage) {
        Scene scene;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLogin.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        try { ms = ManagementSystem.getInstance();}
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}

        //prendo le credenziali
        TextField nomeUtente=(TextField) loader.getNamespace().get("inputUtente");
        PasswordField password=(PasswordField) loader.getNamespace().get("inputPassword");
        Button accedi= (Button) loader.getNamespace().get("accedi");
        accedi.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ms.login(nomeUtente.getText(),password.getText());
                if(ms.getLoggedInUser()==null)
                    JOptionPane.showMessageDialog(null, "NOME UTENTE O PASSWORD NON CORRETTI", "ERRORE", JOptionPane.ERROR_MESSAGE);
                else{
                    new FinestraRicerca().start(new Stage());
                    stage.close();
                }
            }
        });

        nomeUtente.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    ms.login(nomeUtente.getText(),password.getText());
                    if(ms.getLoggedInUser()==null)
                        JOptionPane.showMessageDialog(null, "NOME UTENTE O PASSWORD NON CORRETTI", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    else{
                        new FinestraRicerca().start(new Stage());
                        stage.close();
                    }
                }
            }
        });

        password.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    ms.login(nomeUtente.getText(),password.getText());
                    if(ms.getLoggedInUser()==null)
                        JOptionPane.showMessageDialog(null, "NOME UTENTE O PASSWORD NON CORRETTI", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    else{
                        new FinestraRicerca().start(new Stage());
                        stage.close();
                    }
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
