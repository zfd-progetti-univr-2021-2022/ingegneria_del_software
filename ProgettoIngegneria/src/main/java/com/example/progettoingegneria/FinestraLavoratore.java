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
import javax.swing.JOptionPane;
import java.util.HashSet;
import java.util.Locale;

public class FinestraLavoratore extends Application{
    static Collection<EsperienzaLavorativa> esperienzeLavorative=new HashSet<>();
    Collection<Lingua> lingueParlate=new ArrayList<Lingua>();
    Collection<Patente> patenti=new ArrayList<Patente>();
    Collection<PeriodoDisponibilita> periodiDisponibilita=new ArrayList<PeriodoDisponibilita>();
    static Collection<RecapitoUrgenza> recapitiUrgenze=new ArrayList<RecapitoUrgenza>();
    TextField nome,cognome,codice,luogoNascita,giornoNascita,meseNascita,annoNascita,nazionalita,residenza,recapito,mail,comuniDisp,lingue,periodiDisp,patente;
    CheckBox automunito;
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLavoratore.fxml"));
        try { scene = new Scene(loader.load()); }
        catch (IOException exception) {throw new RuntimeException(exception);}

        nome= (TextField) loader.getNamespace().get("inputNome");
        cognome= (TextField) loader.getNamespace().get("inputCognome");
        codice= (TextField) loader.getNamespace().get("inputCodice");
        luogoNascita= (TextField) loader.getNamespace().get("inputLuogoNascita");
        giornoNascita= (TextField) loader.getNamespace().get("giornoNascita");
        meseNascita= (TextField) loader.getNamespace().get("meseNascita");
        annoNascita= (TextField) loader.getNamespace().get("annoNascita");
        nazionalita= (TextField) loader.getNamespace().get("inputNazionalita");
        residenza= (TextField) loader.getNamespace().get("inputResidenza");
        recapito= (TextField) loader.getNamespace().get("inputRecapito");
        mail= (TextField) loader.getNamespace().get("inputMail");
        lingue=(TextField) loader.getNamespace().get("inputLingue");
        comuniDisp=(TextField) loader.getNamespace().get("inputComuni");
        periodiDisp.setText("Comune1,Comune2");
        periodiDisp= (TextField) loader.getNamespace().get("periodi");
        periodiDisp.setText("1/1/2022-1/2/2022,12/3/2022-12/5/2022");
        patente=(TextField) loader.getNamespace().get("inputPatente");
        automunito=(CheckBox) loader.getNamespace().get("inputAutomunito");

        Button aggiungiContatto = (Button) loader.getNamespace().get("aggiungiContattoLavoratore");
        aggiungiContatto.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraContatto().start(new Stage());
            }
        });

        Button aggiungiEsperienza = (Button) loader.getNamespace().get("aggiungiEsperienza");
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
                inizializzaLingue(lingue.getText());
                inizializzaPatenti(patente.getText());
                inizializzaPeriodi();
                if(recapitiUrgenze.size()>0 && periodiDisponibilita.size()>0 && lingueParlate.size()>0){
                    LocalDate dataNascita=LocalDate.of(Integer.parseInt(annoNascita.getText()),Integer.parseInt(meseNascita.getText()),Integer.parseInt(giornoNascita.getText()));
                    Lavoratore p = Lavoratore.of(nome.getText(), cognome.getText(),luogoNascita.getText(), dataNascita, nazionalita.getText(), mail.getText(),
                            recapito.getText(), residenza.getText(), esperienzeLavorative, lingueParlate, patenti, automunito.isSelected(), periodiDisponibilita, recapitiUrgenze,codice.getText());
                    try {
                        ManagementSystem ms = ManagementSystem.getInstance();
                        if(p.validate().size()==0){
                            ms.addLavoratore(p);
                            esperienzeLavorative.clear();
                            recapitiUrgenze.clear();
                            stage.close();
                        }
                        else
                            JOptionPane.showMessageDialog(null, p.validate().toString(), "ERRORE", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IOException e){System.out.println(e);}
                    catch (URISyntaxException e){System.out.println(e);}
                }
                else{
                    if(recapitiUrgenze.size()==0)
                        JOptionPane.showMessageDialog(null, "INSERISCI ALMENO UN CONTATTO", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    if(periodiDisponibilita.size()==0)
                        JOptionPane.showMessageDialog(null, "ERRORE INSERIMENTO PERIODI", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    if(lingueParlate.size()==0)
                        JOptionPane.showMessageDialog(null, "ERRORE INSERIMENTO LINGUA", "ERRORE", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        stage.setScene(scene);
        stage.setTitle("Registra Lavoratore");
        stage.setResizable(false);
        stage.show();
    }
    //restituisce l'arraylist contenente i contatti, è utilizzato nella finestra del contatto
    public static Collection<RecapitoUrgenza> getRecapitiUrgenze(){return recapitiUrgenze;}
    public static Collection<EsperienzaLavorativa> getEsperienze(){return esperienzeLavorative;}
    private void inizializzaLingue(String lingue) {
        String [] arrLingue=lingue.split(",");
        for(int i=0; i<arrLingue.length; i++)
            try{
                lingueParlate.add(Lingua.valueOf(arrLingue[i].toUpperCase()));
            }
            catch(Exception exception){System.out.println("Errore Lingua");}
    }
    private void  inizializzaPatenti(String patente) {
        String [] arrPatenti=patente.split(",");
        for(int i=0; i<arrPatenti.length; i++){
            try{
                patenti.add(Patente.valueOf(arrPatenti[i].toUpperCase()));
            }
            //non metto una finestra d'errore perchè il lavoratore potrebbe essere sprovvisto di patente
            catch(Exception exception){System.out.println("Patente non valida");}
        }
    }
    private void  inizializzaPeriodi() {
        String [] comuni=comuniDisp.getText().split(",");
        String [] periodi=periodiDisp.getText().split(",");
        String [] singoloPeriodo,inizio,fine;
        LocalDate dataInizio,dataFine;

        for(int i=0; i<periodi.length && comuni.length==periodi.length; i++) {
            try{
                singoloPeriodo=periodi[i].split("-");
                inizio=singoloPeriodo[0].split("/");
                fine=singoloPeriodo[1].split("/");
                dataInizio=LocalDate.of(Integer.parseInt(inizio[2]),Integer.parseInt(inizio[1]),Integer.parseInt(inizio[0]));
                dataFine=LocalDate.of(Integer.parseInt(fine[2]),Integer.parseInt(fine[1]),Integer.parseInt(fine[0]));
                if(PeriodoDisponibilita.of(dataInizio,dataFine,comuni[i].toUpperCase()).validate().size()==0)
                    periodiDisponibilita.add(PeriodoDisponibilita.of(dataInizio,dataFine,comuni[i].toUpperCase()));
            }
            catch(Exception e){System.out.println("Errore periodo");}
        }
    }
}
