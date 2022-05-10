module com.example.progettoingegneria {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires am.ik.yavi;
    requires org.tinylog.api;
    requires java.desktop;


    opens com.example.progettoingegneria to javafx.fxml, com.fasterxml.jackson.databind;
    exports com.example.progettoingegneria;
}
