package hminepos.controller;

import hminepos.database.SqliteHelper;
import hminepos.model.CustomerModel;
import hminepos.model.SalesModel;
import hminepos.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mao on 12/23/2022.
 */


public class SaleReportController implements Initializable {


    @FXML
    private ComboBox<String> cbCustomer;

    @FXML
    private ComboBox<String> cbUser;

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

    private List<SalesModel> salesData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComponents();
        setupTableColumValues();
        showData(true);
    }

    private void showData(boolean fetchFromDB) {
        String strStart = dpStart.getValue().atStartOfDay().toString();
        String strEnd = dpEnd.getValue().atTime(LocalTime.MAX).toString();
        String strVoucher = cbVoucher.getValue();
        String strCustomer = cbCustomer.getValue() == null ? null : cbCustomer.getValue();
        String strUser = cbUser.getValue() == null ? null : cbUser.getValue();
        if (fetchFromDB) {
            // Fetch data from Database
            salesData = SqliteHelper.getSalesData(strStart, strEnd);
            // GROUP BY Java Stream API
            // For Vouchers
            Set<String> voucherList = new HashSet<>();
            voucherList.add(null);
            voucherList.addAll(salesData.stream().collect(Collectors.groupingBy(SalesModel::getVoucher)).keySet());
            // For Customers
            Set<String> customerList = new HashSet<>();
            customerList.add(null);
            customerList.addAll(salesData.stream().collect(Collectors.groupingBy(SalesModel::getCustomerId)).keySet());
            // For Users
            Set<String> userList = new HashSet<>();
            userList.add(null);
            userList.addAll(salesData.stream().collect(Collectors.groupingBy(SalesModel::getCreatedBy)).keySet());

            cbVoucher.setItems(FXCollections.observableArrayList(voucherList));
            cbCustomer.setItems(FXCollections.observableArrayList(customerList));
            cbUser.setItems(FXCollections.observableArrayList(userList));
        }
        List<SalesModel> filterList = salesData;
        // Only filter with time FROM Sql
        // Other filtering options are filtered with Java Stream
        if (strVoucher != null) {
            filterList = salesData.stream().filter(salesModel -> salesModel.getVoucher().equals(strVoucher)).collect(Collectors.toList());
        }
        if (strCustomer != null) {
            filterList = salesData.stream().filter(salesModel -> salesModel.getCustomerId().equals(strCustomer)).collect(Collectors.toList());
        }
        if (strUser != null) {
            filterList = salesData.stream().filter(salesModel -> salesModel.getCreatedBy().equals(strUser)).collect(Collectors.toList());
        }
        tableView.setItems(FXCollections.observableArrayList(filterList));
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

        cbVoucher.showingProperty().addListener(((observable, hidden, showing) -> {
            if (hidden) {
                showData(false);
                System.out.println("User Filter");
            }
        }));

        // init filter by customers
        cbCustomer.showingProperty().addListener((observable, hidden, showing) -> {
            if (hidden) {
                showData(false);
                System.out.println("Customer Filter");
            }
        });

        // init filter by users
        cbUser.showingProperty().addListener((observable, hidden, showing) -> {
            if (hidden) {
                showData(false);
                System.out.println("User Filter");
            }
        });

        dpStart.setOnAction(event -> {
            showData(true);
            System.out.println("Date Start Filter");
        });
        dpEnd.setOnAction(event -> {
            showData(true);
            System.out.println("Date End Filter");
        });



    }

    public void handleClearFilter(ActionEvent event) {
        cbVoucher.setValue(null);
        cbCustomer.setValue(null);
        cbUser.setValue(null);
        showData(false);
    }
}
