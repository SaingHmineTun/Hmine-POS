module it.saimao.hminepos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    requires org.controlsfx.controls;
    requires org.xerial.sqlitejdbc;
    requires com.jfoenix;
    requires MaterialFX;
    requires java.desktop;
    requires password4j;
    requires org.apache.commons.io;

    exports hminepos;
    opens hminepos to javafx.fxml;
    exports hminepos.controller;
    opens hminepos.controller to javafx.fxml;
    exports hminepos.helper;
    opens hminepos.helper to javafx.fxml;
    exports hminepos.model;
    opens hminepos.model to javafx.base;
}