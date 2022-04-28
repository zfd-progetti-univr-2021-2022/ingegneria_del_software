module com.example.progettoingegneria {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.progettoingegneria to javafx.fxml;
    exports com.example.progettoingegneria;
}