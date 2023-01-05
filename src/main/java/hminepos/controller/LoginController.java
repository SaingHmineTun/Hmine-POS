package hminepos.controller;

import hminepos.database.DatabaseHelper;
import hminepos.helper.Utils;
import hminepos.model.UserModel;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

/**
 * Created by Mao on 12/3/2022.
 */


public class LoginController implements Initializable {
    @FXML
    private Text lbFail;

    @FXML
    private MFXButton btLogin;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXTextField tfUserName;

    private Stage stage;

    public void handleLogin(ActionEvent actionEvent) throws IOException {
        String userId = tfUserName.getText();
        String password = pfPassword.getText();

        // User ID must not be empty!
        if (userId.length() == 0) {
            tfUserName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbFail.setText("User ID must not be empty!");
            lbFail.setVisible(true);
            return;
        }

        if (password.length() == 0) {
            pfPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbFail.setText("Password must not be empty!");
            lbFail.setVisible(true);
            return;
        }

        UserModel user = DatabaseHelper.getUserById(userId);

        // Check whether user is null
        if (user == null) {

            tfUserName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbFail.setText("Incorrect User ID!");
            lbFail.setVisible(true);
            return;
        }
        if (Utils.checkPassword(user.getPassword(), password)) {
            // This is Login Success!!!
            Utils.setCurrentUser(user);

            Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
            stage.setX(screenSize.getMinX());
            stage.setY(screenSize.getMinY());
            stage.setWidth(screenSize.getWidth());
            stage.setHeight(screenSize.getHeight());

        } else {
            // Login Failed!!!
            pfPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbFail.setText("Incorrect Password!");
            lbFail.setVisible(true);

        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tfUserName.textProperty().addListener(observable -> {
            if (lbFail.isVisible()) {
                tfUserName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                lbFail.setVisible(false);
            }
        });

        pfPassword.textProperty().addListener(observable -> {
            if (lbFail.isVisible()) {
                pfPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                lbFail.setVisible(false);
            }
        });

        // When press enter key in username, the focus must move to password field
        tfUserName.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER && !tfUserName.getText().isEmpty()) pfPassword.requestFocus();
        });

        pfPassword.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER && !pfPassword.getText().isEmpty())
                btLogin.fire();
        });

    }

    public void handleCreateUser(MouseEvent mouseEvent) throws IOException {

        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/register-user-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((RegisterUserController) fxmlLoader.getController()).setStage(stage);
        stage.setScene(scene);
        stage.setX(screenSize.getMinX());
        stage.setY(screenSize.getMinY());
        stage.setWidth(screenSize.getWidth());
        stage.setHeight(screenSize.getHeight());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
