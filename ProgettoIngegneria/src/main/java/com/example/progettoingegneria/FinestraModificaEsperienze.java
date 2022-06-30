package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Finestra che visualizza tutte le esperienze lavorative
 * per permettere all'utente di vederle e di modificarle.
 */
public class FinestraModificaEsperienze extends Application {
    Lavoratore lavoratore;
    Collection<EsperienzaLavorativa> esperienze=new ArrayList<EsperienzaLavorativa>();
    TableView<EsperienzaLavorativa> tabella;
    ManagementSystem ms;

    /**
     * L'utente desidera vedere/modificare le esperienze lavorative di un lavoratore
     *
     * @param l Lavoratore
     */
    FinestraModificaEsperienze(Lavoratore l){
        super();
        lavoratore=l;
        esperienze=l.getEsperienzeLavorative();
    }

    /**
     * Prepara la finestra per accettare input dall'utente
     *
     * @param stage Finestra
     */
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraModificaEsperienze.fxml"));

        try {
            scene = new Scene(loader.load());
            ms = ManagementSystem.getInstance();
        }
        catch (IOException | URISyntaxException e){
            throw new RuntimeException(e);
        }

        tabella =(TableView) loader.getNamespace().get("tabella");

        // crea header della tabella
        instanziaTabella();

        for(EsperienzaLavorativa e : esperienze)
            tabella.getItems().add(e);

        Button aggiorna= (Button) loader.getNamespace().get("aggiorna");
        aggiorna.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Resetta la tabella: cancella il contenuto
             * e riempi la tabella con tutte le esperienze lavorative.
             *
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                //pulisco la tabella
                for ( int i = 0; i<tabella.getItems().size(); i++)
                    tabella.getItems().clear();

                for(EsperienzaLavorativa e : esperienze)
                    tabella.getItems().add(e);
            }
        });

        stage.setTitle("Visualizza Esperienze");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Prepara l'header della tabella e permetti la modifica delle esperienze lavorative
     * facendo doppio click sulla riga corrispondente.
     * > NOTA: l'utente non puo' modificare esperienze lavorative fatte piu' di 5 anni fa
     */
    private void instanziaTabella() {

        TableColumn<EsperienzaLavorativa,String> inizio = new TableColumn<>("Inizio");
        inizio.setCellValueFactory(new PropertyValueFactory<>("inizioPeriodoLavorativo"));

        TableColumn<EsperienzaLavorativa,String> fine = new TableColumn<>("Fine");
        fine.setCellValueFactory(new PropertyValueFactory<>("finePeriodoLavorativo"));

        TableColumn<EsperienzaLavorativa,String> nome = new TableColumn<>("Azienda");
        nome.setCellValueFactory(new PropertyValueFactory<>("nomeAzienda"));

        TableColumn<EsperienzaLavorativa,String> mansioni = new TableColumn<>("Mansioni");
        mansioni.setCellValueFactory(new PropertyValueFactory<>("mansioniSvolte"));

        TableColumn<EsperienzaLavorativa,String> luogo = new TableColumn<>("Luogo");
        luogo.setCellValueFactory(new PropertyValueFactory<>("luogoLavoro"));

        TableColumn<EsperienzaLavorativa,String> retribuzione = new TableColumn<>("Retribuzione");
        retribuzione.setCellValueFactory(new PropertyValueFactory<>("retribuzioneLordaGiornaliera"));

        tabella.getColumns().addAll(Arrays.asList(nome,inizio,fine,mansioni,luogo,retribuzione));

        // permetti all'utente di modificare le esperienze lavorative facendo doppio click sulla riga corrispondente
        // > NOTA: l'utente non puo' modificare esperienze lavorative fatte piu' di 5 anni fa
        tabella.setRowFactory( tv -> {
            TableRow<EsperienzaLavorativa> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EsperienzaLavorativa rowData = row.getItem();

                    LocalDate adesso = LocalDate.now();
                    LocalDate confronto = adesso.minus(5, ChronoUnit.YEARS);

                    if(rowData.getInizioPeriodoLavorativo().isAfter(confronto) && rowData.getFinePeriodoLavorativo().isAfter(confronto))
                        new FinestraEsperienzaLavorativa(lavoratore,rowData).start(new Stage());
                    else
                        JOptionPane.showMessageDialog(null, "ESPERIENZA NON PIU' MODIFICABILE", "ERRORE", JOptionPane.ERROR_MESSAGE);
                }
            });
            return row;
        });

    }
}
