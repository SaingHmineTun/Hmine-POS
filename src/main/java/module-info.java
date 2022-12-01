module it.saimao.hminepos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;

    exports hminepos;
    opens hminepos to javafx.fxml;
    exports hminepos.controller;
    opens hminepos.controller to javafx.fxml;
    exports hminepos.helper;
    opens hminepos.helper to javafx.fxml;
}