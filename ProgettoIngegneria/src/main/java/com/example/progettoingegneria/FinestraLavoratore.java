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
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.JOptionPane;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * Finestra per aggiungere/modificare un lavoratore.
 */
public class FinestraLavoratore extends Application{
    Lavoratore lavoratore=null;//in caso volessi modificare un lavoratore, questo parametro non sarà null
    boolean modifica=false;//mi dice se sono in modalità modifica o meno
    Collection<EsperienzaLavorativa> esperienzeLavorative=new HashSet<>();
    Collection<Lingua> lingueParlate=new ArrayList<Lingua>();
    Collection<Patente> patenti=new ArrayList<Patente>();
    Collection<PeriodoDisponibilita> periodiDisponibilita=new ArrayList<PeriodoDisponibilita>();
    Collection<RecapitoUrgenza> recapitiUrgenze=new ArrayList<RecapitoUrgenza>();
    TextField nome,cognome,codice,luogoNascita,giornoNascita,meseNascita,annoNascita,nazionalita,residenza,recapito,mail,comuniDisp,lingue,periodiDisp,patente;
    CheckBox automunito;

    /**
     * L'utente vuole inserire un nuovo lavoratore.
     */
    public FinestraLavoratore(){
        super();
    }

    /**
     * L'utente vuole modificare un lavoratore esistente.
     *
     * @param l Lavoratore da modificare
     */
    public FinestraLavoratore(Lavoratore l){
        super();
        lavoratore = l;
        modifica = true;
    }

    /**
     * Imposta la finestra per permettere l'interazione da parte dell'utente.
     *
     * @param stage Finestra
     */
    public void start(Stage stage) {
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FinestraLavoratore.fxml"));
        try {
            scene = new Scene(loader.load());
        }
        catch (IOException exception) {
            throw new RuntimeException(exception);
        }

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
        comuniDisp.setText("Comune1,Comune2");
        periodiDisp= (TextField) loader.getNamespace().get("periodi");
        periodiDisp.setText("1/1/2022-1/2/2022,12/3/2022-12/5/2022");
        patente=(TextField) loader.getNamespace().get("inputPatente");
        automunito=(CheckBox) loader.getNamespace().get("inputAutomunito");

        //controllo se ho aperto la finestra in modalità modifica ed eventualmente prepara i campi compilati
        if(modifica)
            inizializzaLavoratore();

        Button aggiungiContatto = (Button) loader.getNamespace().get("aggiungiContattoLavoratore");
        aggiungiContatto.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * L'utente vuole aggiungere un contatto nuovo: apri la finestra relativa.
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                new FinestraContatto(recapitiUrgenze).start(new Stage());
            }
        });

        Button aggiungiEsperienza = (Button) loader.getNamespace().get("aggiungiEsperienza");
        aggiungiEsperienza.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * L'utente vuole aggiungere una nuova esperienza lavorativa: apri la finestra relativa.
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                new FinestraEsperienzaLavorativa(esperienzeLavorative).start(new Stage());
            }
        });

        Button registraLavoratore= (Button) loader.getNamespace().get("registraLavoratore");
        registraLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * L'utente vuole creare/modificare un lavoratore.
             *
             * @todo Verificare se modifyLavoratore ha successo
             * @todo Assicurarsi che il codice funzioni se viene inserito un valore non intero nei campi della data
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                inizializzaLingue(lingue.getText());
                inizializzaPatenti(patente.getText());
                boolean inizializzaPeriodiSuccess = inizializzaPeriodi();
                if(recapitiUrgenze.size()>0 && periodiDisponibilita.size()>0 && lingueParlate.size()>0){
                    LocalDate dataNascita=LocalDate.of(Integer.parseInt(annoNascita.getText()),Integer.parseInt(meseNascita.getText()),Integer.parseInt(giornoNascita.getText()));
                    Lavoratore p = Lavoratore.of(nome.getText(), cognome.getText(),luogoNascita.getText(), dataNascita, nazionalita.getText(), mail.getText(),
                            recapito.getText(), residenza.getText(), esperienzeLavorative, lingueParlate, patenti, automunito.isSelected(), periodiDisponibilita, recapitiUrgenze,codice.getText());
                    try {
                        ManagementSystem ms = ManagementSystem.getInstance();
                        if(p.validate().size()==0){
                            if(modifica==false)//sto aggiungendo un nuovo lavoratore, non lo sto modificando
                                ms.addLavoratore(p);
                            else
                                System.out.println(ms.modifyLavoratore(p).getStatus());

                            if (inizializzaPeriodiSuccess)
                                stage.close();
                        }
                        else
                            JOptionPane.showMessageDialog(null, p.validate().toString(), "ERRORE", JOptionPane.ERROR_MESSAGE);
                    }
                    catch (IOException e){
                        System.out.println("Errore IOException:" + e);
                    }
                    catch (URISyntaxException e){
                        System.out.println("Errore URISyntaxException: " + e);
                    }
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

        Button visualizzaEsperienze = (Button) loader.getNamespace().get("visualizzaEsperienze");
        visualizzaEsperienze.setVisible(modifica);
        visualizzaEsperienze.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Se il lavoratore e' aperto in modalita' modifica,
             * permetti la modifica delle esperienze lavorative.
             * Apri la finestra relativa.
             *
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                new FinestraModificaEsperienze(lavoratore).start(new Stage());
            }
        });

        Button visualizzaContatti = (Button) loader.getNamespace().get("visualizzaContatti");
        visualizzaContatti.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * L'utente vuole visualizzare i contatti del lavoratore.
             * Apri la finestra relativa.
             *
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                new FinestraVisualizzaContatti(recapitiUrgenze).start(new Stage());
            }
        });

        stage.setScene(scene);
        stage.setTitle("Registra Lavoratore");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * L'utente vuole creare/modificare il lavoratore: recupera le lingue
     * dal campo relativo e aggiungile alla collezione lingueParlate.
     *
     * @param lingue Stringa recuperata dal campo delle lingue parlate
     */
    private void inizializzaLingue(String lingue) {
        lingueParlate.clear();
        String [] arrLingue=lingue.split(",");
        for(int i=0; i<arrLingue.length; i++)
            try{
                lingueParlate.add(Lingua.valueOf(arrLingue[i].toUpperCase().replaceAll(" ","")));
            }
            catch(Exception exception){System.out.println("Errore Lingua");}
    }

    /**
     * L'utente vuole creare/modificare il lavoratore: recupera le patenti
     * dal campo relativo e aggiungile alla collezione patenti.
     *
     * @param patente Stringa recuperata dal campo delle patenti
     */
    private void  inizializzaPatenti(String patente) {
        patenti.clear();
        String [] arrPatenti=patente.split(",");
        for(int i=0; i<arrPatenti.length; i++){
            try{
                patenti.add(Patente.valueOf(arrPatenti[i].toUpperCase()));
            }
            //non metto una finestra d'errore perchè il lavoratore potrebbe essere sprovvisto di patente
            catch(Exception exception){System.out.println("Patente non valida");}
        }
    }

    /**
     * L'utente vuole creare/modificare il lavoratore: recupera i periodi di disponibilita'
     * dai campi relativi e aggiungile alla collezione periodiDisponibilita.
     *
     * @return true se i dati sono stati inseriti e interpretati correttamente, false altrimenti
     */
    private boolean inizializzaPeriodi() {
        periodiDisponibilita.clear();
        String [] comuni=comuniDisp.getText().split(",");
        String [] periodi=periodiDisp.getText().split(",");
        String [] singoloPeriodo,inizio,fine;
        LocalDate dataInizio,dataFine;

        try{
            for(int i=0; i<periodi.length && comuni.length==periodi.length; i++) {
                singoloPeriodo=periodi[i].split("-");
                inizio=singoloPeriodo[0].split("/");
                fine=singoloPeriodo[1].split("/");
                dataInizio=LocalDate.of(Integer.parseInt(inizio[2]),Integer.parseInt(inizio[1]),Integer.parseInt(inizio[0]));
                dataFine=LocalDate.of(Integer.parseInt(fine[2]),Integer.parseInt(fine[1]),Integer.parseInt(fine[0]));
                Comune comuneNormalizzato = normalize(comuni[i]);
                if(PeriodoDisponibilita.of(dataInizio,dataFine,comuneNormalizzato).validate().size()==0)
                    periodiDisponibilita.add(PeriodoDisponibilita.of(dataInizio,dataFine,comuneNormalizzato));
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null, "Errore inserimento periodo di disponibilita'".toUpperCase(), "ERRORE", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /** Permette di rimpiazzare le lettere accentate con "l'equivalente" ASCII */
    private static final Pattern DIACRITICS_AND_FRIENDS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    /**
     * Rimuovi tutte le lettere accentate da str e sostituiscile con "l'equivalente" ASCII.
     * @param str Stringa con lettere accentate
     * @return Stringa senza lettere accentate
     */
    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    /**
     * Sostituisce le lettere accentate nel nome di un comune s con "l'equivalente" ASCII
     * e sostituisce gli spazi e i tratti con un tratto basso.
     *
     * @param s Stringa da normalizzare che rappresenta un comune
     * @return Comune
     */
    private Comune normalize(String s) {
        String noLettereAccentate = stripDiacritics(s);
        noLettereAccentate = noLettereAccentate.replace(" ", "_");
        noLettereAccentate = noLettereAccentate.replace("-", "_");
        noLettereAccentate = noLettereAccentate.toUpperCase();
        return Comune.valueOf(noLettereAccentate);
    }

    /**
     * Se l'utente vuole modificare un lavoratore esistente
     * riempi i campi della finestra con i dati del lavoratore.
     */
    private void inizializzaLavoratore() {
        nome.setText(lavoratore.getNome());
        cognome.setText(lavoratore.getCognome());
        codice.setText(lavoratore.getCodiceFiscale());
        luogoNascita.setText(lavoratore.getLuogoNascita());
        giornoNascita.setText(""+lavoratore.getDataNascita().getDayOfMonth());
        meseNascita.setText(""+lavoratore.getDataNascita().getMonth().getValue());
        annoNascita.setText(""+lavoratore.getDataNascita().getYear());

        nazionalita.setText(lavoratore.getNazionalita());
        residenza.setText(lavoratore.getIndirizzoResidenza());
        recapito.setText(lavoratore.getNumeroTelefono());
        mail.setText(lavoratore.getIndirizzoEmail());

        lingueParlate=lavoratore.getLingueParlate();
        String ling="";
        if (lingueParlate.size() > 0) {
            for (Lingua l:lingueParlate)
                ling=ling+l.toString()+",";
            ling = ling.substring(0,ling.length()-1); //elimino l'ultima virgola
        }
        lingue.setText(ling);

        periodiDisponibilita=lavoratore.getPeriodiDisponibilita();
        String com="";
        String per="";
        if (periodiDisponibilita.size() > 0) {
            for (PeriodoDisponibilita p:periodiDisponibilita){
                com = com + p.getComune() + ",";
                per=per+p.toString()+",";
            }
            com = com.substring(0, com.length() - 1);
            com = com.replace("_", " ");
            per = per.substring(0,per.length()-1);
        }
        comuniDisp.setText(com);
        periodiDisp.setText(per);

        patenti=lavoratore.getPatenti();
        String pat="";
        //potrebbe essere senza patente
        if(patenti.size()>0){
            for (Patente p:patenti)
                pat=pat+p.toString()+",";
            patente.setText(pat.substring(0,pat.length()-1));
        }

        automunito.setSelected(lavoratore.getAutomunito());

        //inizializzo i contatti e le esperienze lavorative
        esperienzeLavorative=lavoratore.getEsperienzeLavorative();
        recapitiUrgenze=lavoratore.getRecapitiUrgenze();
    }
}
