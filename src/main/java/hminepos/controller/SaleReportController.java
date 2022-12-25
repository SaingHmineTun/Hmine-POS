package hminepos.controller;

import hminepos.database.SqliteHelper;
import hminepos.model.CustomerModel;
import hminepos.model.PurchasesModel;
import hminepos.model.SalesModel;
import hminepos.model.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.*;
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
    ComboBox<String> cbSelectDate;

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
        String strCustomer = cbCustomer.getValue();
        String strUser = cbUser.getValue();
        if (fetchFromDB) {
            // Fetch data from Database
            salesData = SqliteHelper.getSalesData(strStart, strEnd);
            // GROUP BY Java Stream API
            // For Vouchers
            List<String> voucherList = new ArrayList<>(salesData.stream().collect(Collectors.groupingBy(SalesModel::getVoucher)).keySet());
            Collections.sort(voucherList);
            // null is added to see the prompt text
            voucherList.add(0, null);

            cbVoucher.setItems(FXCollections.observableArrayList(voucherList));
        }
        List<SalesModel> filterList = salesData;
        // Only filter with time FROM Sql
        // Other filtering options are filtered with Java Stream
        if (strVoucher != null) {
            filterList = filterList.stream().filter(salesModel -> salesModel.getVoucher().equals(strVoucher)).collect(Collectors.toList());
        }
        if (strCustomer != null) {
            // If getValue is "Unknown", change it to empty string
            if (strCustomer.equalsIgnoreCase("unknown")) strCustomer = "";
            String finalStrCustomer = strCustomer;
            filterList = filterList.stream().filter(salesModel -> salesModel.getCustomerId().equals(finalStrCustomer)).collect(Collectors.toList());
        }
        if (strUser != null) {
            filterList = filterList.stream().filter(salesModel -> salesModel.getCreatedBy().equals(strUser)).collect(Collectors.toList());
        }
        tableView.setItems(FXCollections.observableArrayList(filterList));
        tableView.refresh();
        tfTotalAmount.setText("" + tableView.getItems().stream().mapToDouble(SalesModel::getAmount).sum());
        tfTotalQuantity.setText("" + tableView.getItems().stream().mapToInt(SalesModel::getQuantity).sum());
    }

    private void setupTableColumValues() {
        colNo.setCellValueFactory(new PropertyValueFactory<>("no"));
        colVoucher.setCellValueFactory(new PropertyValueFactory<>("voucher"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        // Most rows will not have customer id
        // Because items are sold without specifying them
        // So I make empty customer id to show "Unknown" values
        Callback<TableColumn<SalesModel,String>, TableCell<SalesModel,String>> cellFactory = new Callback<>() {
            @Override
            public TableCell<SalesModel, String> call(TableColumn<SalesModel, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) setGraphic(null);
                        else if (item.isEmpty()) setText("Unknown");
                        else setText(item);
                    }
                };
            }
        };
        colCustomer.setCellFactory(cellFactory);
        colProduct.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colUser.setCellValueFactory(new PropertyValueFactory<>("createdBy"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        colDate.setCellFactory(new Callback<>() {
            @Override
            public TableCell<SalesModel, String> call(TableColumn<SalesModel, String> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) setGraphic(null);
                        else {
                            String dateValue = item.split("T")[0];
                            String timeValue = item.split("T")[1];
                            if (timeValue.contains(".")) timeValue = timeValue.substring(0, timeValue.lastIndexOf("."));
                            setText(dateValue.concat(" ").concat(timeValue));
                        }
                    }
                };
            }
        });
    }

    private void initComponents() {
        cbSelectDate.setItems(getAvailableDates());
        cbSelectDate.getSelectionModel().select(0);

        dpStart.setValue(LocalDate.now());
        dpEnd.setValue(LocalDate.now());

        cbCustomer.getItems().addAll(null, "Unknown");
        cbCustomer.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllCustomers()).stream().map(CustomerModel::getCustomerId).toList());
        cbUser.getItems().add(null);
        cbUser.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllUsers().stream().map(UserModel::getUserId).toList()));

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
        if (cbCustomer.getValue() != null) {
            cbCustomer.setValue(null);
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
