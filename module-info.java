module com.example.bistrobliss {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires spring.context;
    requires spring.beans;
    requires java.sql;
    requires junit;

    opens com.example.bistrobliss to javafx.fxml;
    exports com.example.bistrobliss;
}