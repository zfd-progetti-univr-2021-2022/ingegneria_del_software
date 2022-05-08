package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class FinestraLavoratore extends Application{
    Collection<EsperienzaLavorativa> esperienzeLavorative=new ArrayList<EsperienzaLavorativa>();
    Collection<Lingua> lingueParlate=new ArrayList<Lingua>();
    Collection<Patente> patenti=new ArrayList<Patente>();
    Collection<PeriodoDisponibilita> periodiDisponibilita=new ArrayList<PeriodoDisponibilita>();
    static Collection<RecapitoUrgenza> recapitiUrgenze=new ArrayList<RecapitoUrgenza>();
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLavoratore.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        TextField nome= (TextField) loader.getNamespace().get("inputNome");
        TextField cognome= (TextField) loader.getNamespace().get("inputCognome");
        TextField codice= (TextField) loader.getNamespace().get("inputCodice");
        TextField luogoNascita= (TextField) loader.getNamespace().get("inputLuogoNascita");
        TextField giornoNascita= (TextField) loader.getNamespace().get("giornoNascita");
        TextField meseNascita= (TextField) loader.getNamespace().get("meseNascita");
        TextField annoNascita= (TextField) loader.getNamespace().get("annoNascita");
        TextField nazionalita= (TextField) loader.getNamespace().get("inputNazionalita");
        TextField residenza= (TextField) loader.getNamespace().get("inputResidenza");
        TextField recapito= (TextField) loader.getNamespace().get("inputRecapito");
        TextField mail= (TextField) loader.getNamespace().get("inputMail");
        TextField specializzazioni=(TextField) loader.getNamespace().get("inputSpecializzazioni");
        TextField lingue=(TextField) loader.getNamespace().get("inputLingue");
        TextField periodi= (TextField) loader.getNamespace().get("periodi");
        periodi.setText("01/2022-02/2022,03/2022-05/2022,..");
        TextField patente=(TextField) loader.getNamespace().get("inputPatente");
        CheckBox automunito=(CheckBox) loader.getNamespace().get("inputAutomunito");

        Button aggiungiContatto = (Button) loader.getNamespace().get("AggiungiContattoLavoratore");
        aggiungiContatto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraContatto().start(new Stage());
            }
        });

        Button aggiungiEsperienza= (Button) loader.getNamespace().get("aggiungiEsperienza");
        aggiungiEsperienza.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraEsperienzaLavorativa().start(new Stage());
            }
        });

        Button registraLavoratore= (Button) loader.getNamespace().get("registraLavoratore");
        registraLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(recapitiUrgenze.size()>0){//bisogna inserire almeno un contatto
                    String [] arrLingue=lingue.getText().split(",");
                    String [] arrPatenti=patente.getText().split(",");
                    //aggiungo le lingue parlate
                    for(int i=0; i<arrLingue.length; i++)
                        lingueParlate.add(Lingua.valueOf(arrLingue[i].toUpperCase()));
                    for(int i=0; i<arrPatenti.length; i++)
                        patenti.add(Patente.valueOf(arrPatenti[i].toUpperCase()));
                    LocalDate dataNascita=LocalDate.of(Integer.parseInt(annoNascita.getText()),Integer.parseInt(meseNascita.getText()),Integer.parseInt(giornoNascita.getText()));
                    Lavoratore p = Lavoratore.of(nome.getText(), cognome.getText(),luogoNascita.getText(), dataNascita, nazionalita.getText(), mail.getText(),
                            recapito.getText(), residenza.getText(), esperienzeLavorative, lingueParlate, patenti, automunito.isSelected(), periodiDisponibilita, recapitiUrgenze);
                    try {
                        ManagementSystem ms = ManagementSystem.getInstance();
                        ms.addLavoratore(p);
                    }
                    catch (IOException e){System.out.println(e);}
                    catch (URISyntaxException e){System.out.println(e);}

                    recapitiUrgenze.clear();
                    stage.close();
                }
                else
                    new FinestraErrore().start(new Stage());
            }
        });

        stage.setScene(scene);
        stage.setTitle("Registra Lavoratore");
        stage.setResizable(false);
        stage.show();
    }

    public  Collection<EsperienzaLavorativa> getEsperienze(){return esperienzeLavorative;}
    public static Collection<RecapitoUrgenza> getRecapitiUrgenze(){return recapitiUrgenze;}
}
