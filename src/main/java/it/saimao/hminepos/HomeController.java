package it.saimao.hminepos;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

import static it.saimao.hminepos.Type.*;

public class HomeController {

    public void handle_sales(MouseEvent mouseEvent) {
    }

    public void handle_purchases(MouseEvent mouseEvent) {
    }

    public void handle_products(MouseEvent mouseEvent) throws IOException {

        createStageFor(PRODUCT).show();

    }

    public void handle_users(MouseEvent mouseEvent) throws IOException {
        createStageFor(USER).show();
    }

    private Stage createStageFor(Type type) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = null;
        if (type == USER) {
            fxmlLoader = new FXMLLoader(Main.class.getResource("users-view.fxml"));
            stage.setTitle("Users");
        } else if (type == PRODUCT) {
            fxmlLoader = new FXMLLoader(Main.class.getResource("products-view.fxml"));
            stage.setTitle("Products");
        } else if (type == CUSTOMER) {

            fxmlLoader = new FXMLLoader(Main.class.getResource("customers-view.fxml"));
            stage.setTitle("Customers");
        } else if (type == SUPPLIER) {
            fxmlLoader = new FXMLLoader(Main.class.getResource("suppliers-view.fxml"));
            stage.setTitle("Suppliers");
        }
        Scene scene = new Scene(fxmlLoader.load(), Perc.getFullWidth(), Perc.getFullHeight());
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.initModality(Modality.APPLICATION_MODAL);
        return stage;
    }

    public void handle_history(MouseEvent mouseEvent) {
    }

    public void handle_suppliers(MouseEvent mouseEvent) throws IOException {

        createStageFor(SUPPLIER).show();
    }

    public void handle_customers(MouseEvent mouseEvent) throws IOException {
        createStageFor(CUSTOMER).show();

    }
}