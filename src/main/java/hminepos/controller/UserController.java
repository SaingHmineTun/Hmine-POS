package hminepos.controller;

import com.jfoenix.controls.JFXButton;
import com.password4j.Hash;
import com.password4j.Password;
import hminepos.helper.ImageEncoder;
import hminepos.helper.ImageResizer;
import hminepos.model.UserModel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.utils.BindingUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

/**
 * Created by Mao on 12/1/2022.
 */


public class UserController implements Initializable {


    @FXML
    private JFXButton btAddUser;

    @FXML
    private TableColumn<UserModel, String> colEmail;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private ImageView ivPicture;

    @FXML
    private TableView<?> tableUsers;

    @FXML
    private MFXTextField tfArtistShortcut1;

    @FXML
    private MFXTextField tfEmail;

    @FXML
    private TextField tfFilterById;

    @FXML
    private TextField tfFilterByName;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXPasswordField pfConfirmPassword;

    @FXML
    private MFXTextField tfPhone;

    @FXML
    private MFXTextField tfUserId;

    @FXML
    private MFXTextField tfUserName;

    @FXML
    private HBox updateLayout;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        updateLayout.setVisible(false);
        updateLayout.setManaged(false);
    }

    @FXML
    void handleAddNewUser(ActionEvent event) {

    }

    @FXML
    void handleSelectPicture(ActionEvent event) {
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


    public void handleAddUser(ActionEvent actionEvent) throws IOException {

        UserModel user = new UserModel();
        user.setUserId(tfUserId.getText());
        user.setUserName(tfUserName.getText());
        user.setPhone(tfPhone.getText());
        user.setEmail(tfEmail.getText());
        // TODO : Check whether password and confirm password are similar

        // Hashing password with password4j
        Hash hash = Password.hash(pfPassword.getText()).addRandomSalt(12).withArgon2();
        user.setPassword(hash.getResult());

        // Converting an image file to string
        if (!ivPicture.getImage().getUrl().endsWith("images/user.png")) {
            user.setImage(resizeImage());
        }
        System.out.println(tfUserId.getValidator().isValid());
        System.out.println(user);

    }

    public void handleUpdateUser(ActionEvent actionEvent) {
    }

    public void handleDeleteUser(ActionEvent actionEvent) {
    }

}
