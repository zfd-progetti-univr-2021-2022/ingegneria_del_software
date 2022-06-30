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
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Finestra per inserire o modificare una esperienza lavorativa.
 */
public class FinestraEsperienzaLavorativa extends Application{
    Collection<EsperienzaLavorativa> esperienze=new ArrayList<>();
    Lavoratore lavoratore=null;
    EsperienzaLavorativa esperienza=null;
    boolean modifica=false;
    ManagementSystem ms;
    TextField nome, ubicazione, mansioni, giornoInizio, meseInizio, annoInizio, giornoFine, meseFine, annoFine, retribuzione;

    /**
     * L'utente desidera modificare una esperienza lavorativa di un lavoratore.
     *
     * @param l Lavoratore che possiede l'esperienza lavorativa
     * @param e Esperienza lavorativa da modificare
     */
    public FinestraEsperienzaLavorativa(Lavoratore l, EsperienzaLavorativa e){
        super();
        lavoratore=l;
        esperienza=e;
        modifica=true;
    }

    /**
     * L'utente desidera inserire una esperienza lavorativa.
     *
     * @param c Esperienze lavorative attuali del lavoratore
     */
    public FinestraEsperienzaLavorativa(Collection<EsperienzaLavorativa> c){
        super();
        esperienze=c;
    }

    /**
     * Permetti l'interazione dell'utente impostando azioni sugli elementi della view.
     *
     * @param stage Finestra
     */
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraEsperienzaLavorativa.fxml"));

        try {
            scene = new Scene(loader.load());
            ms = ManagementSystem.getInstance();
        }
        catch (IOException | URISyntaxException exception) {
            throw new RuntimeException(exception);
        }

        nome= (TextField) loader.getNamespace().get("inputNome");
        ubicazione= (TextField) loader.getNamespace().get("inputUbicazione");
        mansioni= (TextField) loader.getNamespace().get("inputMansioni");
        giornoInizio= (TextField) loader.getNamespace().get("giornoInizio");
        meseInizio= (TextField) loader.getNamespace().get("meseInizio");
        annoInizio= (TextField) loader.getNamespace().get("annoInizio");
        giornoFine= (TextField) loader.getNamespace().get("giornoFine");
        meseFine= (TextField) loader.getNamespace().get("meseFine");
        annoFine= (TextField) loader.getNamespace().get("annoFine");
        retribuzione= (TextField) loader.getNamespace().get("inputRetribuzione");

        if(modifica)
            inizializzaEsperienza();

        Button aggiungi= (Button) loader.getNamespace().get("AggiungiEsperienza");
        aggiungi.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Se l'utente ha inserito una data o una retribuzione in formato errato visualizza un messaggio di errore.
             * Altrimenti:
             * - se l'utente vuole inserire una nuova esperienza lavorativa aggiungila a "esperienze" (se tutti i dati inseriti sono validi)
             * - se l'utente vuole modificare una esperienza lavorativa esistente usa il management system per modificarla
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                if(validazioneDate()==false || validazioneRetribuzione()==false)
                    if(validazioneDate()==false)
                        JOptionPane.showMessageDialog(null, "ERRORE INSERIMENTO DATE", "ERRORE", JOptionPane.ERROR_MESSAGE);
                    if(validazioneRetribuzione()==false)
                        JOptionPane.showMessageDialog(null, "ERRORE INSERIMENTO RETRIBUZIONE", "ERRORE", JOptionPane.ERROR_MESSAGE);
                else{
                    LocalDate inizioPeriodoLavorativo=LocalDate.of(Integer.parseInt(annoInizio.getText()),Integer.parseInt(meseInizio.getText()),Integer.parseInt(giornoInizio.getText()));
                    LocalDate finePeriodoLavorativo=LocalDate.of(Integer.parseInt(annoFine.getText()),Integer.parseInt(meseFine.getText()),Integer.parseInt(giornoFine.getText()));

                    EsperienzaLavorativa esp=EsperienzaLavorativa.of(inizioPeriodoLavorativo,finePeriodoLavorativo,nome.getText(), Arrays.asList(mansioni.getText().split(",")),ubicazione.getText(), Double.parseDouble(retribuzione.getText().replaceAll(" ","")));

                    if(modifica==false) {
                        System.out.println("Sto creando una nuova esperienza lavorativa");
                        if (esp.validate().size() == 0) {
                            esperienze.add(esp);
                            stage.close();
                        }
                        else{
                            JOptionPane.showMessageDialog(null, esp.validate(), "ERRORE INSERIMENTO ESPERIENZA LAVORATIVA", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    else{
                        System.out.println("Sto modificando una esperienza lavorativa");
                        try{
                            ManagementSystemResponse response = ms.modifyEsperienzaLavorativa(lavoratore.getCodiceFiscale(), esperienza.getId(), esp);
                            System.out.println("response: " + response);
                            if (response.getStatus() != ManagementSystemStatus.OK){
                                JOptionPane.showMessageDialog(null, esp.validate(), "ERRORE MODIFICA ESPERIENZA LAVORATIVA", JOptionPane.ERROR_MESSAGE);
                            }
                            else{
                                stage.close();
                            }
                        }
                        catch(Exception e){
                            System.out.println("Inserimento fallito");
                        }
                    }
                }
            }
        });

        stage.setScene(scene);
        stage.setTitle("Aggiungi Esperienza");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Se l'utente vuole modificare una esperienza lavorativa,
     * Inserisci i dati attuali dell'esperinza nei campi
     * in modo che l'utente possa modificarli.
     */
    private void inizializzaEsperienza() {

        nome.setText(esperienza.getNomeAzienda());
        ubicazione.setText(esperienza.getLuogoLavoro());

        String mans="";
        for(String s:esperienza.getMansioniSvolte())
            mans=mans+s+",";
        mansioni.setText(mans.substring(0,mans.length()-1));

        giornoInizio.setText(""+esperienza.getInizioPeriodoLavorativo().getDayOfMonth());
        meseInizio.setText(""+esperienza.getInizioPeriodoLavorativo().getMonth().getValue());
        annoInizio.setText(""+esperienza.getInizioPeriodoLavorativo().getYear());

        giornoFine.setText(""+esperienza.getFinePeriodoLavorativo().getDayOfMonth());
        meseFine.setText(""+esperienza.getFinePeriodoLavorativo().getMonth().getValue());
        annoFine.setText(""+esperienza.getFinePeriodoLavorativo().getYear());

        retribuzione.setText(""+esperienza.getRetribuzioneLordaGiornaliera());
    }

    /**
     * Verifica se sono stati inseriti valori interi nei campi delle date.
     *
     * @return true se i campi delle date contengono interi, false altrimenti
     */
    private boolean validazioneDate() {
        try{
            Integer.parseInt(annoInizio.getText());
            Integer.parseInt(meseInizio.getText());
            Integer.parseInt(giornoInizio.getText());
            Integer.parseInt(annoFine.getText());
            Integer.parseInt(meseFine.getText());
            Integer.parseInt(giornoFine.getText());
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    /**
     * Verifica se e' stato inserito un valore double nel campo della retribuzione
     * @return true se il campo contiene un double, false altrimenti
     */
    private boolean validazioneRetribuzione() {
        try{
            Double.parseDouble(retribuzione.getText().replaceAll(" ",""));
            return true;
        }
        catch(Exception e){
            return false;
        }
    }
}
