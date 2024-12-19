module com.example.gui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;

    opens org.example.entity to javafx.base;
    opens com.example.gui to javafx.fxml;
    exports org.example.view;
    opens org.example.view to javafx.fxml;
    exports org.example.view.GUIs;
    opens org.example.view.GUIs to javafx.fxml;
    exports org.example.view.Login;
    opens org.example.view.Login to javafx.fxml;
    exports org.example.view.GUIComponents;
    opens org.example.view.GUIComponents to javafx.fxml;
}