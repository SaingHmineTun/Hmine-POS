package hminepos.controller;

import com.password4j.Hash;
import com.password4j.Password;
import hminepos.database.SqliteHelper;
import hminepos.helper.ImageEncoder;
import hminepos.helper.ImageResizer;
import hminepos.helper.Utils;
import hminepos.model.CustomerModel;
import hminepos.model.UserModel;
import io.github.palexdev.materialfx.controls.MFXButton;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Mao on 12/14/2022.
 */


public class CustomerController implements Initializable {


    @FXML
    private MFXButton btAddCustomer;

    @FXML
    private MFXButton btArtistDelete;

    @FXML
    private MFXButton btArtistUpdate;

    @FXML
    private MFXButton btSelectPicture;

    @FXML
    private TableColumn<CustomerModel, String> colAddress;

    @FXML
    private TableColumn<CustomerModel, String> colCustomerId;

    @FXML
    private TableColumn<CustomerModel, String> colCustomerName;

    @FXML
    private TableColumn<CustomerModel, String> colEmail;

    @FXML
    private TableColumn<CustomerModel, String> colPhone;

    @FXML
    private ImageView ivPicture;

    @FXML
    private BorderPane root;

    @FXML
    private TableView<CustomerModel> tableCustomers;

    @FXML
    private MFXTextField tfAddress;

    @FXML
    private MFXTextField tfCustomerId;

    @FXML
    private MFXTextField tfCustomerName;

    @FXML
    private MFXTextField tfEmail;

    @FXML
    private TextField tfFCustomerId;

    @FXML
    private TextField tfFCustomerName;

    @FXML
    private MFXTextField tfPhone;

    @FXML
    private HBox updateLayout;

    private boolean isUpdatedPicture;

    private ObservableList<CustomerModel> allCustomers;

    private CustomerModel selectedCustomer;

    @FXML
    void handleAddNewCustomer(ActionEvent event) {

        tfCustomerId.clear();
        tfCustomerName.clear();
        tfEmail.clear();
        tfPhone.clear();
        tfAddress.clear();
        ivPicture.setImage(Utils.getDefaultUserImage());
        hideUpdateLayout(true);
        tableCustomers.getSelectionModel().clearSelection();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideUpdateLayout(true);
        setupTableColumValueFactory();
        setupSelectTableRow();
        setupTableFiltering();

        allCustomers = FXCollections.observableArrayList(SqliteHelper.getAllCustomers());
        tableCustomers.setItems(allCustomers);
    }

    private void hideUpdateLayout(boolean hideUpdateLayout) {
        if (hideUpdateLayout) {
            // This is ADD NEW USER
            updateLayout.setVisible(false);
            updateLayout.setManaged(false);
            btAddCustomer.setVisible(true);
            btAddCustomer.setManaged(true);
            tfCustomerId.setEditable(true);

        } else {
            // This is UPDATE USER
            updateLayout.setVisible(true);
            updateLayout.setManaged(true);
            btAddCustomer.setVisible(false);
            btAddCustomer.setManaged(false);
            tfCustomerId.setEditable(false);
        }
    }

    private void setupTableColumValueFactory() {

        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCustomerName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }

    private void setupSelectTableRow() {
        tableCustomers.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) return;
            selectedCustomer = newValue;
            isUpdatedPicture = false;
            tfCustomerId.setText(selectedCustomer.getCustomerId());
            tfCustomerName.setText(selectedCustomer.getCustomerName());
            tfEmail.setText(selectedCustomer.getEmail());
            tfPhone.setText(selectedCustomer.getPhone());
            tfAddress.setText(selectedCustomer.getAddress());
            if (selectedCustomer.getImage() != null && selectedCustomer.getImage().length() > 0) {
                ivPicture.setImage(ImageEncoder.decodeToImage(selectedCustomer.getImage()));
            } else {
                ivPicture.setImage(Utils.getDefaultUserImage());
            }
            hideUpdateLayout(false);
        });
    }

    private void setupTableFiltering() {
        tfFCustomerId.textProperty().addListener(observable -> filterTableUsers());
        tfFCustomerName.textProperty().addListener(observable -> filterTableUsers());
    }

    private void filterTableUsers() {
        List<CustomerModel> filteredList = allCustomers;

        if (!tfFCustomerId.getText().isEmpty())
            filteredList = filteredList.stream().filter(customer -> customer.getCustomerId().contains(tfFCustomerId.getText())).collect(Collectors.toList());

        if (!tfFCustomerName.getText().isEmpty())
            filteredList = filteredList.stream().filter(customer -> customer.getCustomerName().contains(tfFCustomerName.getText())).collect(Collectors.toList());

        tableCustomers.setItems(FXCollections.observableArrayList(filteredList));
        if (filteredList.size() > 0) tableCustomers.getSelectionModel().selectFirst();
    }

    public void handleAddCustomer(ActionEvent actionEvent) throws IOException {



        // Safe to register!!!
        CustomerModel customer = new CustomerModel();
        customer.setCustomerId(tfCustomerId.getText());
        customer.setCustomerName(tfCustomerName.getText());
        customer.setPhone(tfPhone.getText());
        customer.setAddress(tfAddress.getText());
        customer.setEmail(tfEmail.getText());
        // Converting an image file to string
        if (!ivPicture.getImage().getUrl().endsWith("images/user.png")) {
            // Resize image to 128w/128h
            customer.setImage(Utils.resizeImage(ivPicture.getImage().getUrl()));
        }
        // Created By
        customer.setCreatedBy(Utils.getCurrentUserId());
        // Created At
        customer.setCreatedAt(LocalDateTime.now().toString());

        /*
        UserModel object is ready! Let's add to the database!!!
         */
        SqliteHelper.addCustomer(customer);
        refreshTable();
    }

    private void refreshTable() {
        allCustomers = FXCollections.observableArrayList(SqliteHelper.getAllCustomers());
        tableCustomers.setItems(allCustomers);
    }

    public void handleUpdateCustomer(ActionEvent actionEvent) throws IOException {
        CustomerModel customer = new CustomerModel();
        customer.setCustomerId(tfCustomerId.getText());
        customer.setCustomerName(tfCustomerName.getText());
        customer.setAddress(tfAddress.getText());
        customer.setPhone(tfPhone.getText());
        customer.setEmail(tfEmail.getText());
        if (isUpdatedPicture) {
            customer.setImage(Utils.resizeImage(ivPicture.getImage().getUrl()));
        } else {
            customer.setImage(selectedCustomer.getImage());
        }
        if (isUpdatedPicture || validForUpdate(customer)) {
            // Ready to update into database
            SqliteHelper.updateCustomer(customer);
            refreshTable();
        }
    }

    private boolean validForUpdate(CustomerModel customer) {
        return !selectedCustomer.getCustomerName().equals(customer.getCustomerName())
                || !selectedCustomer.getPhone().equals(customer.getPhone())
                || !selectedCustomer.getAddress().equals(customer.getAddress())
                || !selectedCustomer.getEmail().equals(customer.getEmail());
    }

    public void handleClearFilter(ActionEvent actionEvent) {
        tfFCustomerId.clear();
        tfFCustomerName.clear();
    }
}
