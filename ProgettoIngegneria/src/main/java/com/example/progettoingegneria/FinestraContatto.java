package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Rappresenta la finestra per aggiungere un nuovo contatto a un lavoratore.
 */
public class FinestraContatto extends Application{
    Collection <RecapitoUrgenza> recapiti=new ArrayList<RecapitoUrgenza>();

    /**
     * Memorizza recapiti dei contatti attuali
     * per poterne aggiungere uno nuovo.
     * @param c Contatti del lavoratore
     */
    public FinestraContatto(Collection<RecapitoUrgenza> c) {
        super();
        recapiti=c;
    }

    /**
     * Metodo eseguito per visualizzare la finestra e
     * permettere l'interazione da parte dell'utente.
     * @param stage Finestra
     */
    public void start(Stage stage) {
        Scene scene;

        // Recupera dal file FXML gli elementi della view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraContatto.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        TextField nome= (TextField) loader.getNamespace().get("inputNome");
        TextField cognome= (TextField) loader.getNamespace().get("inputCognome");
        TextField mail= (TextField) loader.getNamespace().get("inputMail");
        TextField recapito= (TextField) loader.getNamespace().get("inputRecapito");

        Button aggiungi= (Button) loader.getNamespace().get("AggiungiContatto");

        // imposta azione eseguita quando l'utente clicca sul pulsante "aggiungi"
        aggiungi.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * L'utente ha cliccato sul pulsante "aggiungi" per inserire
             * un nuovo contatto a un lavoratore.
             * Se i dati inseriti vengono validati correttamente il contatto viene inserito.
             * Se i dati inseriti non sono corretti viene visualizzato un messaggio di errore.
             * @param event Evento
             */
            @Override
            public void handle(ActionEvent event) {
                RecapitoUrgenza contatto = RecapitoUrgenza.of(nome.getText(),cognome.getText(), recapito.getText(), mail.getText());
                if(contatto.validate().size()==0){
                    recapiti.add(contatto);
                    stage.close();
                }
                else
                    JOptionPane.showMessageDialog(null, contatto.validate().toString(), "ERRORE", JOptionPane.ERROR_MESSAGE);
            }
        });

        // impostazioni della finestra
        stage.setTitle("Aggiungi Contatto");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
