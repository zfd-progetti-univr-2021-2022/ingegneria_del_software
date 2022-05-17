package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;

public class FinestraRicerca extends Application {
    Collection<Lavoratore> lavoratori=new HashSet<>();
    ManagementSystem ms;
    TextField nome,cognome,lingue,residenza,mansioni,disponibilita;
    //checkbox per la ricerca
    CheckBox checkNome,checkCognome,checkLingue,checkResidenza,checkMansioni,checkDisponibilita,checkPatente,checkAutomunito,patente,automunito;
    HashSet<Lavoratore> supporto=new HashSet<>();//contiene tutti i lavoratori scelti con l'or
    public void start(Stage stage) {
        Scene scene;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraRicerca.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        nome= (TextField) loader.getNamespace().get("inputNome");
        checkNome=(CheckBox) loader.getNamespace().get("checkNome");

        cognome= (TextField) loader.getNamespace().get("inputCognome");
        checkCognome=(CheckBox) loader.getNamespace().get("checkCognome");

        lingue=(TextField) loader.getNamespace().get("inputLingue");
        checkLingue=(CheckBox) loader.getNamespace().get("checkLingue");

        residenza= (TextField) loader.getNamespace().get("inputResidenza");
        checkResidenza=(CheckBox) loader.getNamespace().get("checkMansioni");

        mansioni=(TextField) loader.getNamespace().get("inputMansioni");
        checkMansioni=(CheckBox) loader.getNamespace().get("checkMansioni");

        disponibilita= (TextField) loader.getNamespace().get("inputDisponibilita");
        checkDisponibilita=(CheckBox) loader.getNamespace().get("checkDisponibilita");
        disponibilita.setText("1/1/2022-1/2/2022,Comune");

        patente=(CheckBox) loader.getNamespace().get("inputPatente");
        checkPatente=(CheckBox) loader.getNamespace().get("checkPatente");

        automunito=(CheckBox) loader.getNamespace().get("inputAutomunito");
        checkAutomunito=(CheckBox) loader.getNamespace().get("checkAutomunito");

        Button aggiungiLavoratore= (Button) loader.getNamespace().get("aggiungiLavoratore");
        aggiungiLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraLavoratore().start(new Stage());
            }
        });

        Button aggiungiDipendente= (Button) loader.getNamespace().get("aggiungiDipendente");
        try {
            ms = ManagementSystem.getInstance();
            //se non sei amministratore non ti do la possibilità di aggiungere dipendenti
            if(!(ms.getLoggedInUser().isAdmin()))
                aggiungiDipendente.setVisible(false);
        }
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}

        aggiungiDipendente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere dipendenti
                new FinestraDipendente().start(new Stage());
            }
        });

        TableView tabella =(TableView) loader.getNamespace().get("tabella");
        instanziaTabella(tabella);

        Button cerca= (Button) loader.getNamespace().get("cerca");
        cerca.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //pulisco la tabella
                for ( int i = 0; i<tabella.getItems().size(); i++)
                    tabella.getItems().clear();

                lavoratori.addAll(ms.getLavoratori());
                for(Persona p : lavoratori)
                    addOr((Lavoratore) p);//carico l'array con i lavoratori scelti per caratteristiche or

                for(Lavoratore l : supporto)
                    tabella.getItems().add(l);
                supporto.clear();
            }
        });

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    //metodo per creare l'intestazione della tabella
    private void instanziaTabella(TableView tabella) {
        TableColumn nomeTab = new TableColumn("Nome");
        nomeTab.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn cognomeTab = new TableColumn("Cognome");
        cognomeTab.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn luogoNascitaTab = new TableColumn("Nato a");
        luogoNascitaTab.setCellValueFactory(new PropertyValueFactory<>("luogoNascita"));

        TableColumn dataNascitaTab = new TableColumn("Data");
        dataNascitaTab.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));

        TableColumn nazionalita = new TableColumn("Nazionalità");
        nazionalita.setCellValueFactory(new PropertyValueFactory<>("nazionalita"));

        TableColumn indirizzoEmail = new TableColumn("Mail");
        indirizzoEmail.setCellValueFactory(new PropertyValueFactory<>("indirizzoEmail"));
        indirizzoEmail.setMinWidth(118);

        TableColumn numero = new TableColumn("Numero");
        numero.setCellValueFactory(new PropertyValueFactory<>("numeroTelefono"));

        TableColumn codiceTab = new TableColumn("Codice");
        codiceTab.setCellValueFactory(new PropertyValueFactory<>("codiceFiscale"));
        codiceTab.setMinWidth(130);

        tabella.getColumns().addAll(nomeTab,cognomeTab,luogoNascitaTab,dataNascitaTab,nazionalita,indirizzoEmail,numero,codiceTab);
    }

    private void addOr(Lavoratore a){
        if(!(checkNome.isSelected()) && nome.getText().equals(a.getNome()))
            supporto.add(a);

        if(!(checkCognome.isSelected()) && cognome.getText().equals(a.getCognome()))
            supporto.add(a);

        //controllo se almeno una lingua è contenuta
        if(!(checkLingue.isSelected())){
            String [] arrLingue=lingue.getText().split(",");
            for(int i=0; i<arrLingue.length; i++){
                try{
                    if(a.getLingueParlate().contains(Lingua.valueOf(arrLingue[i].toUpperCase()))) {
                        supporto.add(a);
                        break;
                    }
                }
                catch(Exception e){System.out.println("Errore lingua");}
            }
        }

        if(!(checkResidenza.isSelected()) && residenza.getText().equals(a.getIndirizzoResidenza()))
            supporto.add(a);

        //controllo per ogni esperienza lavorativa le mansioni svolte
        if(!(checkMansioni.isSelected())){
            for(EsperienzaLavorativa e:a.getEsperienzeLavorative()){
                if(e.getMansioniSvolte().contains(mansioni.getText()))
                    supporto.add(a);
            }
        }

        String [] disp,singoloPeriodo,inizio,fine;
        LocalDate dataInizio,dataFine;

        if(!(checkDisponibilita.isSelected())){
            try{
                disp=disponibilita.getText().split(",");
                singoloPeriodo=disp[0].split("-");
                inizio=singoloPeriodo[0].split("/");
                fine=singoloPeriodo[1].split("/");
                dataInizio=LocalDate.of(Integer.parseInt(inizio[2]),Integer.parseInt(inizio[1]),Integer.parseInt(inizio[0]));
                dataFine=LocalDate.of(Integer.parseInt(fine[2]),Integer.parseInt(fine[1]),Integer.parseInt(fine[0]));
                if(a.getPeriodiDisponibilita().contains(PeriodoDisponibilita.of(dataInizio,dataFine,disp[1].toUpperCase())))
                    supporto.add(a);
            }
            catch(Exception exception){System.out.println("Errore disponibilità");}
        }

        //controllo che abbia almeno una patente
        if(!(checkPatente.isSelected()) && patente.isSelected() && a.getPatenti().size()!=0)
            supporto.add(a);

        //controllo che sia automunito
        if(!(checkAutomunito.isSelected()) && checkAutomunito.isSelected() && a.getAutomunito())
            supporto.add(a);
    }
}
