package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.text.Normalizer;
import javafx.stage.WindowEvent;

import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * Finestra principale: permette di effettuare la ricerca dei lavoratori
 * in and e in or e di raggiungere tutte le altre finestre con cui e' possibile:
 * - aggiungere nuovi dipendenti (solo per l'admin) e lavoratori
 * - modificare un lavoratore (aggiungere i contatti e le esperienze lavorative)
 *
 * E' inoltre possibile visualizzare tutti i lavoratori con il pulsante apposito.
 */
public class FinestraRicerca extends Application {
    Collection<Lavoratore> lavoratori=new HashSet<>();
    ManagementSystem ms;
    TextField nome,cognome,lingue,residenza,mansioni,disponibilita;
    //checkbox per la ricerca
    CheckBox checkNome,checkCognome,checkLingue,checkResidenza,checkMansioni,checkDisponibilita,checkPatente,checkAutomunito,patente,automunito;
    HashSet<Lavoratore> supporto=new HashSet<>();//contiene tutti i lavoratori scelti
    TableView<Lavoratore> tabella;

    /**
     * Metodo eseguito per visualizzare la finestra e
     * permettere l'interazione da parte dell'utente.
     * @param stage Finestra
     */
    public void start(Stage stage) {
        Scene scene;

        // Recupera dalla view gli elementi descritti nel file FXML
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
        checkResidenza=(CheckBox) loader.getNamespace().get("checkResidenza");

        mansioni=(TextField) loader.getNamespace().get("inputMansioni");
        checkMansioni=(CheckBox) loader.getNamespace().get("checkMansioni");

        disponibilita= (TextField) loader.getNamespace().get("inputDisponibilita");
        checkDisponibilita=(CheckBox) loader.getNamespace().get("checkDisponibilita");
        disponibilita.setText("1/1/2022-1/2/2022,Comune");

        patente=(CheckBox) loader.getNamespace().get("inputPatente");
        checkPatente=(CheckBox) loader.getNamespace().get("checkPatente");

        automunito=(CheckBox) loader.getNamespace().get("inputAutomunito");
        checkAutomunito=(CheckBox) loader.getNamespace().get("checkAutomunito");

        try {ms = ManagementSystem.getInstance();}
        catch (IOException | URISyntaxException e){throw new RuntimeException(e);}

        tabella =(TableView) loader.getNamespace().get("tabella");

        // crea l'intestazione e inserisci tutti i lavoratori nella tabella
        instanziaTabella();

        // configura pulsante per aggiungere lavoratori
        Button aggiungiLavoratore= (Button) loader.getNamespace().get("aggiungiLavoratore");
        aggiungiLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Apre la finestra per aggiungere contatti.
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                new FinestraLavoratore().start(new Stage());
            }
        });

        // configura il pulsante per aggiungere dipendenti
        Button aggiungiDipendente= (Button) loader.getNamespace().get("aggiungiDipendente");
        //se non sei amministratore non do la possibilità di aggiungere dipendenti
        aggiungiDipendente.setVisible(ms.getLoggedInUser().isAdmin());
        aggiungiDipendente.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Apre la finestra per aggiungere dipendenti.
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                new FinestraDipendente().start(new Stage());
            }
        });

        // configura il pulsante per effettuare la ricerca
        Button cerca= (Button) loader.getNamespace().get("cerca");
        cerca.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //pulisco la tabella
                for ( int i = 0; i<tabella.getItems().size(); i++)
                    tabella.getItems().clear();

                lavoratori.addAll(ms.getLavoratori());

                //aggiungo prima i risultati in and
                supporto.addAll(addAnd());

                //poi i risultati in or
                for(Persona p : lavoratori)
                    addOr((Lavoratore) p);

                //visualizzo nella tabella
                for(Lavoratore l : supporto)
                    tabella.getItems().add(l);

                supporto.clear();
                lavoratori.clear();
            }
        });

        // imposta l'azione per il pulsante "visualizza tutti" i lavoratori
        Button tutti= (Button) loader.getNamespace().get("tutti");
        tutti.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * L'utente vuole visualizzare tutti i
             * lavoratori all'interno della tabella.
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                //pulisco la tabella
                for ( int i = 0; i<tabella.getItems().size(); i++)
                    tabella.getItems().clear();

                for(Lavoratore l : ms.getLavoratori())
                    tabella.getItems().add(l);
            }
        });

        // imposta il pulsante per resettare la tabella e i campi di ricerca
        Button pulisci= (Button) loader.getNamespace().get("pulisci");
        pulisci.setOnAction(new EventHandler<ActionEvent>() {
            /**
             * Quando viene cliccato il pulsante "Pulisci" cancella il contenuto
             * della tabella e resetta i campi di ricerca.
             * @param event Evento click
             */
            @Override
            public void handle(ActionEvent event) {
                //pulisco la tabella
                for ( int i = 0; i<tabella.getItems().size(); i++)
                    tabella.getItems().clear();

                nome.setText("");
                checkNome.setSelected(false);
                cognome.setText("");
                checkCognome.setSelected(false);
                lingue.setText("");
                checkLingue.setSelected(false);
                residenza.setText("");
                checkResidenza.setSelected(false);
                mansioni.setText("");
                checkMansioni.setSelected(false);
                disponibilita.setText("1/1/2022-1/2/2022,Comune");
                checkDisponibilita.setSelected(false);
                patente.setSelected(false);
                checkPatente.setSelected(false);
                automunito.setSelected(false);
                checkAutomunito.setSelected(false);
            }
        });

        // impostazioni della finestra
        stage.setScene(scene);
        stage.setTitle("SELEZIONARE LE CASELLE A DESTRA DI OGNI CAMPO COMPILATO PER EFFETTUARE RICERCHE IN CONGIUNZIONE");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Metodo per creare l'intestazione della tabella e
     * per inserire tutti i lavoratori nella tabella all'inizio del programma.
     */
    public  void instanziaTabella() {
        TableColumn<Lavoratore,String> nomeTab = new TableColumn<>("Nome");
        nomeTab.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Lavoratore,String> cognomeTab = new TableColumn<>("Cognome");
        cognomeTab.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn<Lavoratore,String> luogoNascitaTab = new TableColumn<>("Nato a");
        luogoNascitaTab.setCellValueFactory(new PropertyValueFactory<>("luogoNascita"));

        TableColumn<Lavoratore,String> dataNascitaTab = new TableColumn<>("Data di nascita");
        dataNascitaTab.setCellValueFactory(new PropertyValueFactory<>("dataNascita"));

        TableColumn<Lavoratore,String> nazionalita = new TableColumn<>("Nazionalità");
        nazionalita.setCellValueFactory(new PropertyValueFactory<>("nazionalita"));

        TableColumn<Lavoratore,String> indirizzoEmail = new TableColumn<>("Mail");
        indirizzoEmail.setCellValueFactory(new PropertyValueFactory<>("indirizzoEmail"));
        indirizzoEmail.setMinWidth(118);

        TableColumn<Lavoratore,String> numero = new TableColumn<>("Numero");
        numero.setCellValueFactory(new PropertyValueFactory<>("numeroTelefono"));

        TableColumn<Lavoratore,String> codiceTab = new TableColumn<>("Codice");
        codiceTab.setCellValueFactory(new PropertyValueFactory<>("codiceFiscale"));
        codiceTab.setMinWidth(130);

        // prepara l'intestazione
        tabella.getColumns().addAll(Arrays.asList(nomeTab,cognomeTab,luogoNascitaTab,dataNascitaTab,nazionalita,indirizzoEmail,numero,codiceTab));

        // inserisci tutti i lavoratori
        for(Lavoratore l : ms.getLavoratori())
            tabella.getItems().add(l);

        // quando l'utente fa doppio click su un lavoratore vuol dire che vuole modificarlo:
        // apri la finestra per modificare il lavoratore.
        // > Quando verra' chiusa fai il refresh della tabella
        tabella.setRowFactory( tv -> {
            TableRow<Lavoratore> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Lavoratore rowData = row.getItem();
                    Stage finestraLavoratoreStage = new Stage();
                    finestraLavoratoreStage.setOnHidden(new EventHandler<WindowEvent>() {
                        /**
                         * Quando la finestra viene chiusa cancella gli elementi
                         * della tabella e re-inserisci tutti i lavoratori.
                         * @param event Chiusura della finestra
                         */
                        @Override
                        public void handle(WindowEvent event) {
                            //pulisco la tabella
                            for ( int i = 0; i<tabella.getItems().size(); i++)
                                tabella.getItems().clear();

                            for (Lavoratore l : ms.getLavoratori())
                                tabella.getItems().add(l);
                        }
                    });

                    new FinestraLavoratore(rowData).start(finestraLavoratoreStage);
                }
            });
            return row;
        });
    }

    /** Regex per convertire lettere accentate in "equivalenti" lettere ASCII */
    private static final Pattern DIACRITICS_AND_FRIENDS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    /**
     * Sostituisce le lettere accentate contenute
     * nella stringa passata come parametro con "l'equivalente" ASCII.
     * @param str Stringa con lettere accentate
     * @return Stringa con lettere ASCII
     */
    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    /**
     * Dato il nome reale di un comune converti:
     * - le lettere accentate "nell'equivalente" ASCII (es. è -> e)
     * - gli spazi in tratti bassi
     * - i tratti in tratti bassi
     *
     * Il risultato e' il nome del comune all'interno dell'enumerazione Comune
     *
     * @param s Nome del comune con spazi e lettere accentate
     * @return Comune dell'enumerazione
     */
    private Comune normalize(String s) {
        String noLettereAccentate = stripDiacritics(s);
        noLettereAccentate = noLettereAccentate.replace(" ", "_");
        noLettereAccentate = noLettereAccentate.replace("-", "_");
        noLettereAccentate = noLettereAccentate.toUpperCase();
        return Comune.valueOf(noLettereAccentate);
    }

    /**
     * Metodo per la ricerca in and dei lavoratori.
     * In base alle caselle selezionate effettua una ricerca in and:
     * ottiene dal management system tutti i lavoratori che hanno le caratteristiche
     * inserite da intefaccia grafica.
     * @return Collezione di lavoratori con le caratteristiche ricercate in and
     */
    private Collection<Lavoratore> addAnd(){
        String name=null;
        String surname=null;
        String luogoResidenza=null;
        Collection<Lingua> languages =null;
        Collection<PeriodoDisponibilita> periodiDisponibilita=null;
        Collection<String> job=null;
        Collection<Patente> patenti=null;
        Boolean auto= null;

        if(!lingue.getText().equals(""))
            languages =new ArrayList<>();

        if(!disponibilita.getText().equals(""))
            periodiDisponibilita =new ArrayList<>();

        if(checkNome.isSelected())
            name=nome.getText();

        if(checkCognome.isSelected())
            surname=cognome.getText();

        if(checkLingue.isSelected()){
            String [] arrLingue=lingue.getText().split(",");
            for(int i=0; i<arrLingue.length; i++){
                try{languages.add(Lingua.valueOf(arrLingue[i].toUpperCase()));}
                catch(Exception e){System.out.println("Errore lingua");}
            }
        }

        String [] disp,singoloPeriodo,inizio,fine;
        LocalDate dataInizio,dataFine;
        if(checkDisponibilita.isSelected()){
            try{
                disp=disponibilita.getText().split(",");//array diviso in date e comune alla fine
                singoloPeriodo=disp[0].split("-");
                inizio=singoloPeriodo[0].split("/");
                fine=singoloPeriodo[1].split("/");
                dataInizio=LocalDate.of(Integer.parseInt(inizio[2]),Integer.parseInt(inizio[1]),Integer.parseInt(inizio[0]));
                dataFine=LocalDate.of(Integer.parseInt(fine[2]),Integer.parseInt(fine[1]),Integer.parseInt(fine[0]));
                periodiDisponibilita.add(PeriodoDisponibilita.of(dataInizio,dataFine, normalize(disp[1])));
            }
            catch(Exception exception){System.out.println("Errore disponibilità");}
        }

        if(checkMansioni.isSelected())
            job= Arrays.asList(mansioni.getText().split(","));

        if(checkResidenza.isSelected())
            luogoResidenza=residenza.getText();

        //se ciò non è verificato verrà passato una collection di patenti vuota
        if(checkPatente.isSelected() && patente.isSelected()){
            patenti =new ArrayList<>();
            patenti.add(Patente.valueOf("B"));//aggiungo una patente per far capire che voglio i lavoratori con almeno una patente
        }

        if(checkAutomunito.isSelected() && automunito.isSelected())
            auto=Boolean.valueOf(true);

        if(checkAutomunito.isSelected() && !(automunito.isSelected()))
            auto=Boolean.valueOf(false);


        //creo il management system
        try {ms = ManagementSystem.getInstance();}
        catch (IOException | URISyntaxException e){throw new RuntimeException(e);}

        if(name==null&&surname==null&&luogoResidenza==null&&languages==null&&periodiDisponibilita==null&&job==null&&auto==null&&patenti==null)
            return new HashSet<Lavoratore>();
        else
            return ms.selectLavoratoriAnd(name, surname, languages, periodiDisponibilita,job,luogoResidenza, auto, patenti);
    }

    /**
     * Metodo per la ricerca in or dei lavoratori.
     * Se il lavoratore a ha almeno una delle caratteristiche
     * ricercate in or dall'utente inseriscilo nel set "supporto".
     * @param a Lavoratore
     */
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
                if(a.getPeriodiDisponibilita().contains(PeriodoDisponibilita.of(dataInizio,dataFine, normalize(disp[1]))))
                    supporto.add(a);
            }
            catch(Exception exception){System.out.println("Errore disponibilità");}
        }

        //controllo che abbia almeno una patente,(ignoro il caso in cui nessuna delle due caselle è selezionata)
        if(!(checkPatente.isSelected()) && patente.isSelected() && a.getPatenti().size()!=0)
            supporto.add(a);

        //controllo che sia automunito, (ignoro il caso in cui nessuna delle due caselle è selezionata)
        if(!(checkAutomunito.isSelected()) && checkAutomunito.isSelected() && a.getAutomunito())
            supporto.add(a);
    }
}
