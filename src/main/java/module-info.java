module it.saimao.hminepos {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires java.net.http;

    requires org.controlsfx.controls;
    requires org.xerial.sqlitejdbc;
    requires com.jfoenix;
    requires MaterialFX;
    requires java.desktop;
    requires password4j;
    requires com.google.gson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires org.mongodb.bson.record.codec;

    exports hminepos;
    opens hminepos to javafx.fxml;
    exports hminepos.controller;
    opens hminepos.controller to javafx.fxml;
    exports hminepos.helper;
    opens hminepos.helper to javafx.fxml;
    exports hminepos.model;
    opens hminepos.model to javafx.base, com.google.gson;
}