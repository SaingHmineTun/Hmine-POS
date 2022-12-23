package hminepos.controller;

import hminepos.database.SqliteHelper;
import hminepos.helper.Utils;
import hminepos.model.CustomerModel;
import hminepos.model.SalesModel;
import hminepos.model.UserModel;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

/**
 * Created by Mao on 12/23/2022.
 */


public class SaleReportController implements Initializable {


    @FXML
    private ComboBox<CustomerModel> cbCustomer;

    @FXML
    private ComboBox<UserModel> cbUser;

    @FXML
    private ComboBox<String> cbVoucher;

    @FXML
    private TableColumn<SalesModel, Double> colAmount;

    @FXML
    private TableColumn<SalesModel, String> colCustomer;

    @FXML
    private TableColumn<SalesModel, String> colDate;

    @FXML
    private TableColumn<SalesModel, Integer> colNo;

    @FXML
    private TableColumn<SalesModel, Double> colPrice;

    @FXML
    private TableColumn<SalesModel, String> colProduct;

    @FXML
    private TableColumn<SalesModel, Integer> colQuantity;

    @FXML
    private TableColumn<SalesModel, String> colUser;

    @FXML
    private TableColumn<SalesModel, String> colVoucher;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private DatePicker dpStart;

    @FXML
    private TableView<SalesModel> tableView;

    private ObservableList<SalesModel> salesData;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComponents();
        setupTableColumValues();
        showData();
    }

    private void showData() {
        salesData = FXCollections.observableArrayList(SqliteHelper.getSalesData(dpStart.getValue().atStartOfDay(), dpEnd.getValue().atTime(LocalTime.MAX)));
        tableView.setItems(salesData);
    }

    private void setupTableColumValues() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colVoucher.setCellValueFactory(new PropertyValueFactory<>("voucher"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    private void initComponents() {
        dpStart.setValue(LocalDate.now());
        dpEnd.setValue(LocalDate.now());

        // init filter by customers
        cbCustomer.getItems().add(null);
        cbCustomer.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllCustomers()));
        cbCustomer.showingProperty().addListener((observable, hidden, showing) -> {
            // TODO : Customer Filter (Sale Report)
        });


        // init filter by users
        cbUser.getItems().add(null);
        cbUser.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllUsers()));
        cbUser.showingProperty().addListener((observable, hidden, showing) -> {
            // TODO : User Filter (Sale Report)
        });

    }

    public void handleFilter(ActionEvent actionEvent) {
        showData();
    }
}
