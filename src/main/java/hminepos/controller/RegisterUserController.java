package hminepos.controller;

import com.password4j.Hash;
import com.password4j.Password;
import hminepos.database.SqliteHelper;
import hminepos.helper.ImageEncoder;
import hminepos.helper.ImageResizer;
import hminepos.model.UserModel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by Mao on 12/5/2022.
 */


public class RegisterUserController implements Initializable {
    @FXML
    private ImageView ivPicture;

    @FXML
    private Text lbStatus;

    @FXML
    private MFXPasswordField pfCPassword;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXTextField tfEmail;

    @FXML
    private MFXTextField tfPhone;

    @FXML
    private MFXTextField tfUserId;

    @FXML
    private MFXTextField tfUserName;


    private Stage stage;

    @FXML
    void handleRegister(ActionEvent event) throws IOException {

        // User Id must not be empty
        if (tfUserId.getText().length() == 0) {
            showLabelStatus("User ID must not be empty!");
            return;
        }

        // Check UserId, if already existed, it must fail to register!
        if (SqliteHelper.getUserById(tfUserId.getText()) != null) {
            showLabelStatus("User ID already existed!");
        }

        // Username must not be empty!
        if (tfUserName.getText().length() == 0) {
            showLabelStatus("User Name must not be empty!");
            return;
        }

        // Password must not be empty
        if (pfPassword.getText().length() == 0) {
            showLabelStatus("Password must not be empty!");
            return;
        }

        // Confirm password must not be empty!
        if (pfCPassword.getText().length() == 0) {
            showLabelStatus("Confirm password must not be empty!");
            return;
        }

        // Password and Confirm Password must be equal
        if (!pfCPassword.getText().equals(pfPassword.getText())) {
            showLabelStatus("Password do not match!");
            return;
        }



        // Safe to register user!
        UserModel user = new UserModel();
        user.setUserId(tfUserId.getText());
        user.setUserName(tfUserName.getText());
        user.setPhone(tfPhone.getText());
        user.setEmail(tfEmail.getText());

        // Hashing password with password4j
        Hash hash = Password.hash(pfPassword.getText()).addRandomSalt(12).withArgon2();
        user.setPassword(hash.getResult());
        // Converting an image file to string
        if (!ivPicture.getImage().getUrl().endsWith("images/user.png")) {
            // Resize image to 128w/128h
            user.setImage(resizeImage());
        }
        if (SqliteHelper.addUser(user)) {
            // Register Success! Leave to Login
            handleGotoLogin(null);
        }


    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tfUserId.textProperty().addListener(observable -> hideLbStatus());
        tfUserName.textProperty().addListener(observable -> hideLbStatus());
        tfPhone.textProperty().addListener(observable -> hideLbStatus());
        tfEmail.textProperty().addListener(observable -> hideLbStatus());
        pfPassword.textProperty().addListener(observable -> hideLbStatus());
    }

    private void hideLbStatus() {
        if (lbStatus.isVisible()) lbStatus.setVisible(false);
    }

    public void handleGotoLogin(MouseEvent mouseEvent) throws IOException {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((LoginController) fxmlLoader.getController()).setStage(stage);
        stage.setScene(scene);
        stage.setX(screenSize.getMinX());
        stage.setY(screenSize.getMinY());
        stage.setWidth(screenSize.getWidth());
        stage.setHeight(screenSize.getHeight());
    }


    @FXML
    void handleChoosePicture(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image files (*.png, *.jpg, *.jpeg)", "*.png", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(ivPicture.getParent().getScene().getWindow());
        if (file != null) {
            ivPicture.setImage(new Image(file.getAbsolutePath()));
        }
    }

    private String resizeImage() throws IOException {
        String inputUrl = ivPicture.getImage().getUrl();
        String outputUrl = inputUrl.substring(0, inputUrl.lastIndexOf('.'));
        String type = inputUrl.substring(inputUrl.lastIndexOf(".") + 1);
        outputUrl = outputUrl + "_compressed." + type;
        ImageResizer.resize(inputUrl, outputUrl, 128, 128);
        String resizedImage = ImageEncoder.encodeToString(new Image(outputUrl), type);
        Files.deleteIfExists(Paths.get(outputUrl));
        return resizedImage;
    }

    private void showLabelStatus(String message) {
        lbStatus.setText(message);
        lbStatus.setVisible(true);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
