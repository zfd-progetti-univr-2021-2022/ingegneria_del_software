package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.awt.event.*;
import java.text.Normalizer;
import javafx.stage.WindowEvent;

import java.time.LocalDate;
import java.util.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class FinestraRicerca extends Application {
    Collection<Lavoratore> lavoratori=new HashSet<>();
    ManagementSystem ms;
    TextField nome,cognome,lingue,residenza,mansioni,disponibilita;
    //checkbox per la ricerca
    CheckBox checkNome,checkCognome,checkLingue,checkResidenza,checkMansioni,checkDisponibilita,checkPatente,checkAutomunito,patente,automunito;
    HashSet<Lavoratore> supporto=new HashSet<>();//contiene tutti i lavoratori scelti
    TableView<Lavoratore> tabella;
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
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}

        tabella =(TableView) loader.getNamespace().get("tabella");
        instanziaTabella();

        Button aggiungiLavoratore= (Button) loader.getNamespace().get("aggiungiLavoratore");
        aggiungiLavoratore.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere contatti
                new FinestraLavoratore().start(new Stage());
            }
        });

        Button aggiungiDipendente= (Button) loader.getNamespace().get("aggiungiDipendente");
        //se non sei amministratore non do la possibilità di aggiungere dipendenti
        aggiungiDipendente.setVisible(ms.getLoggedInUser().isAdmin());
        aggiungiDipendente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //apro la finestra per aggiungere dipendenti
                new FinestraDipendente().start(new Stage());
            }
        });

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

        Button tutti= (Button) loader.getNamespace().get("tutti");
        tutti.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //pulisco la tabella
                for ( int i = 0; i<tabella.getItems().size(); i++)
                    tabella.getItems().clear();

                for(Lavoratore l : ms.getLavoratori())
                    tabella.getItems().add(l);
            }
        });

        Button pulisci= (Button) loader.getNamespace().get("pulisci");
        pulisci.setOnAction(new EventHandler<ActionEvent>() {
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
                disponibilita.setText("");
                checkDisponibilita.setSelected(false);
                patente.setSelected(false);
                checkPatente.setSelected(false);
                automunito.setSelected(false);
                checkAutomunito.setSelected(false);
            }
        });

        stage.setScene(scene);
        stage.setTitle("SELEZIONARE LE CASELLE A DESTRA DI OGNI CAMPO COMPILATO PER EFFETTUARE RICERCHE IN CONGIUNZIONE");
        stage.setResizable(false);
        stage.show();
    }

    //metodo per creare l'intestazione della tabella
    public  void instanziaTabella() {
        TableColumn nomeTab = new TableColumn("Nome");
        nomeTab.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn cognomeTab = new TableColumn("Cognome");
        cognomeTab.setCellValueFactory(new PropertyValueFactory<>("cognome"));

        TableColumn luogoNascitaTab = new TableColumn("Nato a");
        luogoNascitaTab.setCellValueFactory(new PropertyValueFactory<>("luogoNascita"));

        TableColumn dataNascitaTab = new TableColumn("Data di nascita");
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

        for(Lavoratore l : ms.getLavoratori())
            tabella.getItems().add(l);

        tabella.setRowFactory( tv -> {
            TableRow<Lavoratore> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Lavoratore rowData = row.getItem();
                    Stage finestraLavoratoreStage = new Stage();
                    finestraLavoratoreStage.setOnHidden(new EventHandler<WindowEvent>() {
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

    private static final Pattern DIACRITICS_AND_FRIENDS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    private Comune normalize(String s) {
        String noLettereAccentate = stripDiacritics(s);
        noLettereAccentate = noLettereAccentate.replace(" ", "_");
        noLettereAccentate = noLettereAccentate.replace("-", "_");
        noLettereAccentate = noLettereAccentate.toUpperCase();
        return Comune.valueOf(noLettereAccentate);
    }

    //metodo per la ricerca in and dei lavoratori
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
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}

        if(name==null&&surname==null&&luogoResidenza==null&&languages==null&&periodiDisponibilita==null&&job==null&&auto==null&&patenti==null)
            return new HashSet<Lavoratore>();
        else
            return ms.selectLavoratoriAnd(name, surname, languages, periodiDisponibilita,job,luogoResidenza, auto, patenti);
    }

    //metodo per la ricerca in or dei lavoratori
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
