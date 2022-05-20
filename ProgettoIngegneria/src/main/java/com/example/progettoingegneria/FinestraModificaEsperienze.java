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
import java.util.ArrayList;
import java.util.Collection;

public class FinestraModificaEsperienze extends Application {
    Lavoratore lavoratore;
    Collection<EsperienzaLavorativa> esperienze=new ArrayList<EsperienzaLavorativa>();
    TableView tabella;
    ManagementSystem ms;

    FinestraModificaEsperienze(Lavoratore l,Collection<EsperienzaLavorativa> c){super();lavoratore=l;esperienze=c;}

    public void start(Stage stage) {
        Scene scene;
        stage.setTitle("Visualizza Esperienze");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraModificaEsperienze.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        try {
            ms = ManagementSystem.getInstance();
        }
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}

        tabella =(TableView) loader.getNamespace().get("tabella");
        instanziaTabella();

        for(EsperienzaLavorativa e : esperienze)
            tabella.getItems().add(e);

        Button aggiorna= (Button) loader.getNamespace().get("aggiorna");
        aggiorna.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //pulisco la tabella
                for ( int i = 0; i<tabella.getItems().size(); i++)
                    tabella.getItems().clear();

                for(EsperienzaLavorativa e : lavoratore.getEsperienzeLavorative())
                    tabella.getItems().add(e);
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void instanziaTabella() {

        TableColumn inizio = new TableColumn("Inizio");
        inizio.setCellValueFactory(new PropertyValueFactory<>("inizioPeriodoLavorativo"));

        TableColumn fine = new TableColumn("Fine");
        fine.setCellValueFactory(new PropertyValueFactory<>("finePeriodoLavorativo"));

        TableColumn nome = new TableColumn("Azienda");
        nome.setCellValueFactory(new PropertyValueFactory<>("nomeAzienda"));

        TableColumn mansioni = new TableColumn("Mansioni");
        mansioni.setCellValueFactory(new PropertyValueFactory<>("mansioniSvolte"));

        TableColumn luogo = new TableColumn("Luogo");
        luogo.setCellValueFactory(new PropertyValueFactory<>("luogoLavoro"));

        TableColumn retribuzione = new TableColumn("Retribuzione");
        retribuzione.setCellValueFactory(new PropertyValueFactory<>("retribuzioneLordaGiornaliera"));


        tabella.getColumns().addAll(nome,inizio,fine,mansioni,luogo,retribuzione);

        tabella.setRowFactory( tv -> {
            TableRow<EsperienzaLavorativa> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    EsperienzaLavorativa rowData = row.getItem();
                    new FinestraEsperienzaLavorativa(lavoratore,rowData).start(new Stage());
                }
            });
            return row ;
        });

    }
}
