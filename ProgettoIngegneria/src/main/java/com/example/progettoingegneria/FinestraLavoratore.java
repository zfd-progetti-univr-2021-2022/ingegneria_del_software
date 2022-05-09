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
        TextField comuni=(TextField) loader.getNamespace().get("inputComuni");
        TextField lingue=(TextField) loader.getNamespace().get("inputLingue");
        TextField periodi= (TextField) loader.getNamespace().get("periodi");
        periodi.setText("1/1/2022-1/2/2022,12/3/2022-12/5/2022");
        TextField patente=(TextField) loader.getNamespace().get("inputPatente");
        CheckBox automunito=(CheckBox) loader.getNamespace().get("inputAutomunito");

        Button aggiungiContatto = (Button) loader.getNamespace().get("aggiungiContattoLavoratore");
        aggiungiContatto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraContatto().start(new Stage());
            }
        });

        Button registraLavoratore= (Button) loader.getNamespace().get("registraLavoratore");
        registraLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(recapitiUrgenze.size()>0){//bisogna inserire almeno un contatto
                    LocalDate dataNascita=LocalDate.of(Integer.parseInt(annoNascita.getText()),Integer.parseInt(meseNascita.getText()),Integer.parseInt(giornoNascita.getText()));
                    inizializzaLingue(lingue.getText());
                    inizializzaPatenti(patente.getText());
                    inizializzaPeriodi(periodi.getText(),comuni.getText());
                    Lavoratore p = Lavoratore.of(nome.getText(), cognome.getText(),luogoNascita.getText(), dataNascita, nazionalita.getText(), mail.getText(),
                            recapito.getText(), residenza.getText(), esperienzeLavorative, lingueParlate, patenti, automunito.isSelected(), periodiDisponibilita, recapitiUrgenze,codice.getText());
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
    //restituisce l'arraylist contenente i contatti, è utilizzato nella finestra del contatto
    public static Collection<RecapitoUrgenza> getRecapitiUrgenze(){return recapitiUrgenze;}
    private void inizializzaLingue(String lingue) {
        String [] arrLingue=lingue.split(",");
        for(int i=0; i<arrLingue.length; i++)
            lingueParlate.add(Lingua.valueOf(arrLingue[i].toUpperCase()));
    }
    private void  inizializzaPatenti(String patente) {
        String [] arrPatenti=patente.split(",");
        for(int i=0; i<arrPatenti.length; i++)
            patenti.add(Patente.valueOf(arrPatenti[i].toUpperCase()));
    }
    private void  inizializzaPeriodi(String periodo, String comuni) {
        String [] periodi=periodo.split(",");
        String [] singoloPeriodo,inizio,fine;
        LocalDate dataInizio,dataFine;

        for(int i=0; i<periodi.length; i++) {
            singoloPeriodo=periodi[i].split("-");
            inizio=singoloPeriodo[0].split("/");
            fine=singoloPeriodo[1].split("/");
            dataInizio=LocalDate.of(Integer.parseInt(inizio[2]),Integer.parseInt(inizio[1]),Integer.parseInt(inizio[0]));
            dataFine=LocalDate.of(Integer.parseInt(fine[2]),Integer.parseInt(fine[1]),Integer.parseInt(fine[0]));
            periodiDisponibilita.add(PeriodoDisponibilita.of(dataInizio,dataFine,comuni));
        }
    }
}
