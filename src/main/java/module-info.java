module it.saimao.hminepos {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires java.desktop;

    opens it.saimao.hminepos to javafx.fxml;
    exports it.saimao.hminepos;
}