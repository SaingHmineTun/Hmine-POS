package hminepos.controller;

import com.jfoenix.controls.JFXButton;
import hminepos.database.DatabaseHelper;
import hminepos.database.DatabaseHelper;
import hminepos.helper.ImageEncoder;
import hminepos.helper.Utils;
import hminepos.model.SupplierModel;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Mao on 12/16/2022.
 */


public class SupplierController implements Initializable {


    @FXML
    private TableColumn<SupplierModel, String> colAddress;

    @FXML
    private TableColumn<SupplierModel, String> colEmail;

    @FXML
    private TableColumn<SupplierModel, String> colPhone;

    @FXML
    private TableColumn<SupplierModel, String> colSupplierId;

    @FXML
    private TableColumn<SupplierModel, String> colSupplierName;

    @FXML
    private TextField filterById;

    @FXML
    private TextField filterByName;

    @FXML
    private JFXButton btAddSupplier;

    @FXML
    private ImageView ivPicture;

    @FXML
    private BorderPane root;

    @FXML
    private TableView<SupplierModel> tableSuppliers;

    @FXML
    private MFXTextField tfAddress;

    @FXML
    private MFXTextField tfEmail;

    @FXML
    private MFXTextField tfPhone;

    @FXML
    private MFXTextField tfSupplierId;

    @FXML
    private MFXTextField tfSupplierName;

    @FXML
    private HBox updateLayout;

    private SupplierModel selectedSupplier;

    private ObservableList<SupplierModel> allSuppliers;

    private boolean isUpdatedPicture;

    @FXML
    void handleAddNewSupplier(ActionEvent event) {

        tfSupplierId.clear();
        tfSupplierName.clear();
        tfEmail.clear();
        tfPhone.clear();
        tfAddress.clear();
        ivPicture.setImage(Utils.getDefaultUserImage());
        hideUpdateLayout(true);
        tableSuppliers.getSelectionModel().clearSelection();
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

    @FXML
    void handleUpdateSupplier(ActionEvent event) throws IOException {
        SupplierModel supplier = new SupplierModel();
        supplier.setSupplierId(tfSupplierId.getText());
        supplier.setSupplierName(tfSupplierName.getText());
        supplier.setAddress(tfAddress.getText());
        supplier.setPhone(tfPhone.getText());
        supplier.setEmail(tfEmail.getText());
        if (isUpdatedPicture) {
            supplier.setImage(Utils.resizeImage(ivPicture.getImage().getUrl()));
        } else {
            supplier.setImage(selectedSupplier.getImage());
        }
        if (isUpdatedPicture || validForUpdate(supplier)) {
            // Ready to update into database
            DatabaseHelper.updateSupplier(supplier);
            refreshTable();
        }
    }

    private boolean validForUpdate(SupplierModel supplier) {
        return !selectedSupplier.getSupplierName().equals(supplier.getSupplierName())
                || !selectedSupplier.getPhone().equals(supplier.getPhone())
                || !selectedSupplier.getAddress().equals(supplier.getAddress())
                || !selectedSupplier.getEmail().equals(supplier.getEmail());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        hideUpdateLayout(true);
        setupTableColumValueFactory();
        setupSelectTableRow();
        setupTableFiltering();

        allSuppliers = FXCollections.observableArrayList(DatabaseHelper.getAllSuppliers());
        tableSuppliers.setItems(allSuppliers);
    }

    private void setupTableFiltering() {
        filterById.textProperty().addListener(observable -> filterTableSuppliers());
        filterByName.textProperty().addListener(observable -> filterTableSuppliers());
    }

    private void filterTableSuppliers() {
        List<SupplierModel> filteredList = allSuppliers;

        if (!filterById.getText().isEmpty())
            filteredList = filteredList.stream().filter(supplier -> supplier.getSupplierId().contains(filterById.getText())).collect(Collectors.toList());

        if (!filterByName.getText().isEmpty())
            filteredList = filteredList.stream().filter(supplier -> supplier.getSupplierName().contains(filterByName.getText())).collect(Collectors.toList());

        tableSuppliers.setItems(FXCollections.observableArrayList(filteredList));
        if (filteredList.size() > 0) tableSuppliers.getSelectionModel().selectFirst();
    }

    private void setupSelectTableRow() {
        tableSuppliers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) return;
            selectedSupplier = newValue;
            isUpdatedPicture = false;
            tfSupplierId.setText(selectedSupplier.getSupplierId());
            tfSupplierName.setText(selectedSupplier.getSupplierName());
            tfEmail.setText(selectedSupplier.getEmail());
            tfPhone.setText(selectedSupplier.getPhone());
            tfAddress.setText(selectedSupplier.getAddress());
            if (selectedSupplier.getImage() != null && selectedSupplier.getImage().length() > 0) {
                ivPicture.setImage(ImageEncoder.decodeToImage(selectedSupplier.getImage()));
            } else {
                ivPicture.setImage(Utils.getDefaultUserImage());
            }
            hideUpdateLayout(false);
        });
    }

    private void setupTableColumValueFactory() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    private void hideUpdateLayout(boolean bool) {

        if (bool) {
            // This is ADD NEW USER
            updateLayout.setVisible(false);
            updateLayout.setManaged(false);
            btAddSupplier.setVisible(true);
            btAddSupplier.setManaged(true);
            tfSupplierId.setEditable(true);

        } else {
            // This is UPDATE USER
            updateLayout.setVisible(true);
            updateLayout.setManaged(true);
            btAddSupplier.setVisible(false);
            btAddSupplier.setManaged(false);
            tfSupplierId.setEditable(false);
        }
    }

    public void handleAddSupplier(ActionEvent actionEvent) throws IOException {

        // Safe to register!!!
        SupplierModel supplier = new SupplierModel();
        supplier.setSupplierId(tfSupplierId.getText());
        supplier.setSupplierName(tfSupplierName.getText());
        supplier.setAddress(tfAddress.getText());
        supplier.setPhone(tfPhone.getText());
        supplier.setEmail(tfEmail.getText());
        // Converting an image file to string
        if (!ivPicture.getImage().getUrl().endsWith("images/user.png")) {
            // Resize image to 128w/128h
            supplier.setImage(Utils.resizeImage(ivPicture.getImage().getUrl()));
        }
        // Created By
        supplier.setCreatedBy(Utils.getCurrentUserId());
        // Created At
        supplier.setCreatedAt(LocalDateTime.now().toString());

        /*
        SupplierModel object is ready! Let's add to the database!!!
         */
        DatabaseHelper.addSupplier(supplier);
        refreshTable();
    }

    private void refreshTable() {
        allSuppliers = FXCollections.observableArrayList(DatabaseHelper.getAllSuppliers());
        tableSuppliers.setItems(allSuppliers);
    }

    public void handleClearFilter(ActionEvent actionEvent) {
        filterByName.clear();
        filterById.clear();
    }
}
