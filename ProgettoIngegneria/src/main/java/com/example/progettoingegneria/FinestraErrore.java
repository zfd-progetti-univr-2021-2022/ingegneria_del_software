package com.example.progettoingegneria;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class FinestraErrore extends Application{
    public void start(Stage a){
        BorderPane root = new BorderPane(new Label("INSERIRE ALMENO UN CONTATTO"));
        Scene s = new Scene(root,200,100);
        a.setScene(s);
        a.setTitle("Errore");
        a.setResizable(false);
        a.show();
    }
}
