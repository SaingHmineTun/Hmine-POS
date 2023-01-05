package hminepos.controller;

import com.jfoenix.controls.JFXButton;
import com.password4j.Hash;
import com.password4j.Password;
import hminepos.database.DatabaseHelper;
import hminepos.helper.ImageEncoder;
import hminepos.helper.ImageResizer;
import hminepos.helper.Utils;
import hminepos.model.UserModel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static io.github.palexdev.materialfx.validation.Validated.INVALID_PSEUDO_CLASS;

/**
 * Created by Mao on 12/1/2022.
 */


public class UserController implements Initializable {


    @FXML
    private JFXButton btAddUser;

    @FXML
    private TableColumn<UserModel, String> colEmail;

    @FXML
    private TableColumn<UserModel, String> colPhone;

    @FXML
    private TableColumn<UserModel, String> colUserId;

    @FXML
    private TableColumn<UserModel, String> colUserName;

    @FXML
    private ImageView ivPicture;

    @FXML
    private TableView<UserModel> tableUsers;

    @FXML
    private MFXTextField tfEmail;

    @FXML
    private TextField tfFilterById;

    @FXML
    private TextField tfFilterByName;

    @FXML
    private MFXPasswordField pfPassword;

    @FXML
    private MFXTextField tfPhone;

    @FXML
    private MFXTextField tfUserId;

    @FXML
    private MFXTextField tfUserName;

    @FXML
    private HBox updateLayout;
    @FXML
    private Label lbPassword;

    @FXML
    private Label lbUserId;

    @FXML
    private Label lbUserName;

    private ObservableList<UserModel> allUsers;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hideUpdateLayout(true);
        setupTableColumValueFactory();
        setupSelectTableRow();
        setupTableFiltering();
        setupTextFieldListeners();
        allUsers = FXCollections.observableArrayList(DatabaseHelper.getAllUsers());
        tableUsers.setItems(allUsers);
    }

    private void setupTextFieldListeners() {
        tfUserId.textProperty().addListener(observable -> hideErrorLabels());
        tfUserName.textProperty().addListener(observable -> hideErrorLabels());
        tfEmail.textProperty().addListener(observable -> hideErrorLabels());
        tfPhone.textProperty().addListener(observable -> hideErrorLabels());
        pfPassword.textProperty().addListener(observable -> hideErrorLabels());
    }

    private void hideErrorLabels() {

        if (lbUserId.isVisible()) {
            lbUserId.setVisible(false);
            lbUserId.setManaged(false);
            tfUserId.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        }
        if (lbUserName.isVisible()) {
            lbUserName.setVisible(false);
            lbUserName.setManaged(false);
            tfUserName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        }
        if (lbPassword.isVisible()) {
            lbPassword.setVisible(false);
            lbPassword.setManaged(false);
            pfPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
        }
    }

    private void setupTableFiltering() {
        tfFilterById.textProperty().addListener(observable -> filterTableUsers());
        tfFilterByName.textProperty().addListener(observable -> filterTableUsers());
    }

    private void filterTableUsers() {
        List<UserModel> filteredList = allUsers;

        if (!tfFilterById.getText().isEmpty())
            filteredList = filteredList.stream().filter(user -> user.getUserId().contains(tfFilterById.getText())).collect(Collectors.toList());

        if (!tfFilterByName.getText().isEmpty())
            filteredList = filteredList.stream().filter(user -> user.getUserName().contains(tfFilterByName.getText())).collect(Collectors.toList());

        tableUsers.setItems(FXCollections.observableArrayList(filteredList));
        if (filteredList.size() > 0) tableUsers.getSelectionModel().selectFirst();
    }

    private void hideUpdateLayout(boolean hideUpdateLayout) {
        if (hideUpdateLayout) {
            // This is ADD NEW USER
            updateLayout.setVisible(false);
            updateLayout.setManaged(false);
            btAddUser.setVisible(true);
            btAddUser.setManaged(true);
            pfPassword.setVisible(true);
            pfPassword.setManaged(true);
            tfUserId.setEditable(true);

        } else {
            // This is UPDATE USER
            updateLayout.setVisible(true);
            updateLayout.setManaged(true);
            btAddUser.setVisible(false);
            btAddUser.setManaged(false);
            pfPassword.setVisible(false);
            pfPassword.setManaged(false);
            tfUserId.setEditable(false);
        }
    }

    private void setupTableColumValueFactory() {

        colUserId.setCellValueFactory(new PropertyValueFactory<>("userId"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }
    private boolean isUpdatedPicture;
    private UserModel currentUser;

    private void setupSelectTableRow() {
        tableUsers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) return;
            isUpdatedPicture = false;
            currentUser = newValue;
            tfUserId.setText(currentUser.getUserId());
            tfUserName.setText(currentUser.getUserName());
            tfEmail.setText(currentUser.getEmail());
            tfPhone.setText(currentUser.getPhone());
            if (currentUser.getImage() != null && currentUser.getImage().length() > 0) {
                ivPicture.setImage(ImageEncoder.decodeToImage(currentUser.getImage()));
            } else {
                ivPicture.setImage(Utils.getDefaultUserImage());
            }

            hideUpdateLayout(false);

        });



    }


    @FXML
    void handleAddNewUser(ActionEvent event) {

        tfUserId.clear();
        tfUserName.clear();
        tfEmail.clear();
        tfPhone.clear();
        pfPassword.clear();
        ivPicture.setImage(Utils.getDefaultUserImage());
        hideUpdateLayout(true);
        tableUsers.getSelectionModel().clearSelection();

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
        isUpdatedPicture = true;
    }

    public void handleAddUser(ActionEvent actionEvent) throws IOException {

        // User Id must not be empty
        if (tfUserId.getText().length() == 0) {
            tfUserId.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbUserId.setText("User ID must not be empty!");
            lbUserId.setVisible(true);
            lbUserId.setManaged(true);
            return;
        }

        // Check UserId, if already existed, it must fail to register!
        if (DatabaseHelper.getUserById(tfUserId.getText()) != null) {
            tfUserId.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbUserId.setText("User ID already existed!");
            lbUserId.setVisible(true);
            lbUserId.setManaged(true);
            return;
        }

        // Username must not be empty!
        if (tfUserName.getText().length() == 0) {
            tfUserName.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbUserName.setVisible(true);
            lbUserName.setManaged(true);
            return;
        }

        // Password must not be empty
        if (pfPassword.getText().length() == 0) {
            pfPassword.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
            lbPassword.setVisible(true);
            lbPassword.setManaged(true);
            return;
        }

        // Safe to register!!!
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
            user.setImage(Utils.resizeImage(ivPicture.getImage().getUrl()));
        }

        /*
        UserModel object is ready! Let's add to the database!!!
         */
        DatabaseHelper.addUser(user);
        refreshTable();

    }


    public void handleUpdateUser(ActionEvent actionEvent) throws IOException {
        UserModel user = new UserModel();
        user.setUserId(tfUserId.getText());
        user.setUserName(tfUserName.getText());
        user.setPhone(tfPhone.getText());
        user.setEmail(tfEmail.getText());
        if (isUpdatedPicture) {
            user.setImage(Utils.resizeImage(ivPicture.getImage().getUrl()));
        } else {
            user.setImage(currentUser.getImage());
        }
        if (isUpdatedPicture || validForUpdate(user)) {
            // Ready to update into database
            DatabaseHelper.updateUser(user);
            refreshTable();
        }
    }


    public void handleDeleteUser(ActionEvent actionEvent) {
    }

    private void refreshTable() {
        allUsers = FXCollections.observableArrayList(DatabaseHelper.getAllUsers());
        tableUsers.setItems(allUsers);
    }


    private boolean validForUpdate(UserModel user) {
        return !currentUser.getUserName().equals(user.getUserName())
                || !currentUser.getPhone().equals(user.getPhone())
                || !currentUser.getEmail().equals(user.getEmail());
    }

    public void handleClearFilter(ActionEvent actionEvent) {
        if (tfFilterByName.getText().length() > 0) tfFilterByName.clear();
        if (tfFilterById.getText().length() > 0) tfFilterById.clear();
    }
}
