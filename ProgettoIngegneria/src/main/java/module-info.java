module com.example.progettoingegneria {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires am.ik.yavi;


    opens com.example.progettoingegneria to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.progettoingegneria;
}
