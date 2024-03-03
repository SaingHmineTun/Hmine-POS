package hminepos;

import hminepos.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((LoginController)fxmlLoader.getController()).setStage(stage);
        stage.setTitle("Hmine POS");
        stage.getIcons().add(new Image(getClass().getResource("/images/HminePOS.png").toExternalForm()));
        stage.setScene(scene);
        stage.setX(screenSize.getMinX());
        stage.setY(screenSize.getMinY());
        stage.setWidth(screenSize.getWidth());
        stage.setHeight(screenSize.getHeight());
        stage.setMaximized(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}