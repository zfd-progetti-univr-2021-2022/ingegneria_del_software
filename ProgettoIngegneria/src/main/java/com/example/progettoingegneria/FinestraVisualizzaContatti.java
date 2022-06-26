package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Rappresenta la finestra che permette di visualizzare i contatti di un lavoratore.
 */
public class FinestraVisualizzaContatti extends Application {
    Collection<RecapitoUrgenza> contatti;
    TableView<RecapitoUrgenza> tabella;

    /**
     * L'utente vuole visualizzare i contatti di un lavoratore.
     * @param c Contatti del lavoratore
     */
    public FinestraVisualizzaContatti(Collection<RecapitoUrgenza> c){
        super();
        contatti=c;
    }

    /**
     * Metodo eseguito per visualizzare la finestra e
     * permettere l'interazione da parte dell'utente.
     * @param stage Finestra
     */
    public void start(Stage stage) {
        Scene scene;

        // Recupera gli elementi dalla view descritta nel file FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraVisualizzaContatti.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        tabella =(TableView) loader.getNamespace().get("tabella");

        // crea header della tabella
        instanziaTabella();

        // inserisci i contatti attualmente esistenti
        for(RecapitoUrgenza e : contatti)
            tabella.getItems().add(e);

        // impostazioni della finestra
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Contatti");
        stage.show();
    }

    /**
     * Crea header della tabella con il formato:
     * | Nome | Cognome | Numero di Telefono | Mail |
     */
    private void instanziaTabella() {

        TableColumn<RecapitoUrgenza,String> nome = new TableColumn<>("Nome");
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<RecapitoUrgenza,String> cognome = new TableColumn<>("Cognome");
        cognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn<RecapitoUrgenza,String> numeroTelefono = new TableColumn<>("Numero di Telefono");
        numeroTelefono.setCellValueFactory(new PropertyValueFactory<>("numeroTelefono"));

        TableColumn<RecapitoUrgenza,String> indirizzoEmail = new TableColumn<>("Mail");
        indirizzoEmail.setCellValueFactory(new PropertyValueFactory<>("indirizzoEmail"));

        tabella.getColumns().addAll(Arrays.asList(nome,cognome,numeroTelefono,indirizzoEmail));
    }
}
