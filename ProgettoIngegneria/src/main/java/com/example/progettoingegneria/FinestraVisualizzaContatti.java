package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Collection;

public class FinestraVisualizzaContatti extends Application {
    Collection<RecapitoUrgenza> contatti;
    TableView tabella;
    public FinestraVisualizzaContatti(Collection<RecapitoUrgenza> c){super();contatti=c;}

    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraVisualizzaContatti.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        tabella =(TableView) loader.getNamespace().get("tabella");
        instanziaTabella();

        for(RecapitoUrgenza e : contatti)
            tabella.getItems().add(e);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Contatti");
        stage.show();
    }

    private void instanziaTabella() {

        TableColumn nome = new TableColumn("Nome");
        nome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn cognome = new TableColumn("Cognome");
        cognome.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn numeroTelefono = new TableColumn("Numero di Telefono");
        numeroTelefono.setCellValueFactory(new PropertyValueFactory<>("numeroTelefono"));

        TableColumn indirizzoEmail = new TableColumn("Mail");
        indirizzoEmail.setCellValueFactory(new PropertyValueFactory<>("indirizzoEmail"));

        tabella.getColumns().addAll(nome,cognome,numeroTelefono,indirizzoEmail);

    }
}
