package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

public class FinestraErrore extends Application{
    public void start(Stage a){
        BorderPane root=null;
        try {
            ManagementSystem ms = ManagementSystem.getInstance();
            if(ms.getLoggedInUser()==null)
                root = new BorderPane(new Label("NOME UTENTE O PASSWORD NON CORRETTI"));
            else{
                root = new BorderPane(new Label("AGGIUNGI ALMENO UN CONTATTO"));
            }
        }
        catch (IOException e){System.out.println(e);}
        catch (URISyntaxException e){System.out.println(e);}
        Scene s = new Scene(root,300,100);
        a.setScene(s);
        a.setTitle("Errore");
        a.setResizable(false);
        a.show();
    }
}
