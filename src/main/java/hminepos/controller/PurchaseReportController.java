package hminepos.controller;

import hminepos.database.SqliteHelper;
import hminepos.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Mao on 12/23/2022.
 */


public class PurchaseReportController implements Initializable {


    @FXML
    private ComboBox<String> cbSupplier;

    @FXML
    private ComboBox<String> cbUser;

    @FXML
    private ComboBox<String> cbVoucher;

    @FXML
    ComboBox<String> cbSelectDate;

    @FXML
    private TableColumn<PurchasesModel, Double> colAmount;

    @FXML
    private TableColumn<PurchasesModel, String> colSupplier;

    @FXML
    private TableColumn<PurchasesModel, String> colDate;

    @FXML
    private TableColumn<PurchasesModel, Integer> colNo;

    @FXML
    private TableColumn<PurchasesModel, Double> colPrice;

    @FXML
    private TableColumn<PurchasesModel, String> colProduct;

    @FXML
    private TableColumn<PurchasesModel, Integer> colQuantity;

    @FXML
    private TableColumn<PurchasesModel, String> colUser;

    @FXML
    private TableColumn<PurchasesModel, String> colVoucher;

    @FXML
    private DatePicker dpEnd;

    @FXML
    private DatePicker dpStart;

    @FXML
    private TableView<PurchasesModel> tableView;

    private List<PurchasesModel> purchasesModelList;

    @FXML
    private TextField tfTotalAmount;

    @FXML
    private TextField tfTotalQuantity;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initComponents();
        setupTableColumValues();
        showData(true);
    }

    private void showData(boolean fetchFromDB) {
        System.out.println("Show Data");
        String strStart = dpStart.getValue().atStartOfDay().toString();
        String strEnd = dpEnd.getValue().atTime(LocalTime.MAX).toString();
        String strVoucher = cbVoucher.getValue();
        String strSupplier = cbSupplier.getValue();
        String strUser = cbUser.getValue();
        if (fetchFromDB) {
            // Fetch data from Database
            purchasesModelList = SqliteHelper.getPurchaseData(strStart, strEnd);
            // GROUP BY Java Stream API
            // For Vouchers
            List<String> voucherList = new ArrayList<>(purchasesModelList.stream().collect(Collectors.groupingBy(PurchasesModel::getVoucher)).keySet());
            Collections.sort(voucherList);
            // null is added to see the prompt text
            voucherList.add(0, null);

            cbVoucher.setItems(FXCollections.observableArrayList(voucherList));
        }
        List<PurchasesModel> filterList = purchasesModelList;
        // Only filter with time FROM Sql
        // Other filtering options are filtered with Java Stream
        if (strVoucher != null) {
            filterList = filterList.stream().filter(salesModel -> salesModel.getVoucher().equals(strVoucher)).collect(Collectors.toList());
        }
        if (strSupplier != null)
            filterList = filterList.stream().filter(salesModel -> salesModel.getSupplierId().equals(strSupplier)).collect(Collectors.toList());
        if (strUser != null) {
            filterList = filterList.stream().filter(salesModel -> salesModel.getCreatedBy().equals(strUser)).collect(Collectors.toList());
        }
        tableView.setItems(FXCollections.observableArrayList(filterList));
        tableView.refresh();
        tfTotalAmount.setText("" + tableView.getItems().stream().mapToDouble(PurchasesModel::getAmount).sum());
        tfTotalQuantity.setText("" + tableView.getItems().stream().mapToInt(PurchasesModel::getQuantity).sum());
    }

    private void setupTableColumValues() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colVoucher.setCellValueFactory(new PropertyValueFactory<>("voucher"));
        colSupplier.setCellValueFactory(new PropertyValueFactory<>("supplierId"));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

    private void initComponents() {
        cbSelectDate.setItems(getAvailableDates());
        cbSelectDate.getSelectionModel().select(0);

        dpStart.setValue(LocalDate.now());
        dpEnd.setValue(LocalDate.now());

        cbSupplier.getItems().addAll(null, "Unknown");
        cbSupplier.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllSuppliers()).stream().map(SupplierModel::getSupplierId).toList());
        cbUser.getItems().add(null);
        cbUser.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllUsers().stream().map(UserModel::getUserId).toList()));

        cbVoucher.showingProperty().addListener(((observable, hidden, showing) -> {
            if (hidden) {
                showData(false);
                System.out.println("Voucher Filter");
            }
        }));

        // init filter by customers
        cbSupplier.showingProperty().addListener((observable, hidden, showing) -> {
            if (hidden) {
                showData(false);
                System.out.println("Supplier Filter");
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

    private ObservableList<String> getAvailableDates() {
        List<String> list = Arrays.asList("Today", "Yesterday", "Two days ago", "Three days ago", "This Week (Mon-Sun)", "This Month", "Last Month", "This Year", "Last Year");
        return FXCollections.observableArrayList(list);
    }

    public void handleClearFilter(ActionEvent event) {
        boolean callShowData = false;
        if (cbVoucher.getValue() != null) {
            cbVoucher.setValue(null);
            callShowData = true;
        }
        if (cbSupplier.getValue() != null) {
            cbSupplier.setValue(null);
            callShowData = true;
        }
        if (cbUser.getValue() != null) {
            cbUser.setValue(null);
            callShowData = true;
        }
        // Prevent calling showData method everytime
        // When you click Clear Filter
        if (callShowData) showData(false);
    }

    public void handleSelectDate(ActionEvent actionEvent) {
        System.out.println("Index: " + cbSelectDate.getSelectionModel().getSelectedIndex());
        int selectedIndex = cbSelectDate.getSelectionModel().getSelectedIndex();
        switch (selectedIndex) {
            case 0 -> {
                dpStart.setValue(LocalDate.now());
                dpEnd.setValue(LocalDate.now());
            }
            case 1 -> {
                dpStart.setValue(LocalDate.now().minusDays(1));
                dpEnd.setValue(LocalDate.now().minusDays(1));
            }
            case 2 -> {
                dpStart.setValue(LocalDate.now().minusDays(2));
                dpEnd.setValue(LocalDate.now().minusDays(2));
            }
            case 3 -> {
                dpStart.setValue(LocalDate.now().minusDays(3));
                dpEnd.setValue(LocalDate.now().minusDays(3));
            }
            case 4 -> {
                dpStart.setValue(LocalDate.now().with(DayOfWeek.MONDAY));
                dpEnd.setValue(LocalDate.now().with(DayOfWeek.SUNDAY));
            }
            case 5 -> {
                dpStart.setValue(LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()));
                dpEnd.setValue(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
            }
            case 6 -> {
                dpStart.setValue(LocalDate.now().minusMonths(1).with(TemporalAdjusters.firstDayOfMonth()));
                dpEnd.setValue(LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth()));
            }
            case 7 -> {
                dpStart.setValue(LocalDate.now().with(TemporalAdjusters.firstDayOfYear()));
                dpEnd.setValue(LocalDate.now().with(TemporalAdjusters.lastDayOfYear()));
            }
            case 8 -> {
                dpStart.setValue(LocalDate.now().minusYears(1).with(TemporalAdjusters.firstDayOfYear()));
                dpEnd.setValue(LocalDate.now().minusYears(1).with(TemporalAdjusters.lastDayOfYear()));
            }
        }
        showData(true);
    }
}
