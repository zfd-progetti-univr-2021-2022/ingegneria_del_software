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

/**
 * Rappresenta la finestra dove l'utente puo' effettuare l'accesso al Management System.
 * Questo e' l'entrypoint dell'applicazione.
 */
public class FinestraLogin extends Application{
    private ManagementSystem ms;
    public void start(Stage stage) {
        Scene scene;

        // Recupera dal file FXML gli elementi della view e crea l'istanza del Management System
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLogin.fxml"));
        try {
            scene = new Scene(loader.load());
            ms = ManagementSystem.getInstance();
        }
        catch (IOException | URISyntaxException exception) {
            throw new RuntimeException("Errore lettura/scrittura file json/fxml: " + exception);
        }

        TextField nomeUtente=(TextField) loader.getNamespace().get("inputUtente");
        PasswordField password=(PasswordField) loader.getNamespace().get("inputPassword");
        Button accedi= (Button) loader.getNamespace().get("accedi");

        // imposta azioni da eseguire in base agli eventi scatenati su field e pulsante

        accedi.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Quando l'utente clicca il pulsante recupera le credenziali dalla view e
             * tenta di effettuare l'accesso.
             * Se viene effettuato l'accesso apri la finestra di ricerca dei lavoratori,
             * altrimenti visualizza un messaggio di errore.
             *
             * @param event Evento
             */
            @Override
            public void handle(ActionEvent event) {
                if(!ms.login(nomeUtente.getText(), password.getText()))
                    JOptionPane.showMessageDialog(null, "NOME UTENTE O PASSWORD NON CORRETTI", "ERRORE", JOptionPane.ERROR_MESSAGE);
                else{
                    new FinestraRicerca().start(new Stage());
                    stage.close();
                }
            }
        });

        nomeUtente.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Quando l'utente preme il tasto enter sulla tastiera recupera le credenziali dalla view e
             * tenta di effettuare l'accesso.
             * Se viene effettuato l'accesso apri la finestra di ricerca dei lavoratori,
             * altrimenti visualizza un messaggio di errore.
             *
             * @param ke Evento
             */
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    if(!ms.login(nomeUtente.getText(), password.getText()))
                        JOptionPane.showMessageDialog(null, "NOME UTENTE O PASSWORD NON CORRETTI", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    else{
                        new FinestraRicerca().start(new Stage());
                        stage.close();
                    }
                }
            }
        });

        password.setOnKeyPressed(new EventHandler<KeyEvent>() {
            /**
             * Quando l'utente preme il tasto enter sulla tastiera recupera le credenziali dalla view e
             * tenta di effettuare l'accesso.
             * Se viene effettuato l'accesso apri la finestra di ricerca dei lavoratori,
             * altrimenti visualizza un messaggio di errore.
             *
             * @param ke Evento
             */
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    if(!ms.login(nomeUtente.getText(), password.getText()))
                        JOptionPane.showMessageDialog(null, "NOME UTENTE O PASSWORD NON CORRETTI", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    else{
                        new FinestraRicerca().start(new Stage());
                        stage.close();
                    }
                }
            }
        });

        // impostazioni della finestra
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
