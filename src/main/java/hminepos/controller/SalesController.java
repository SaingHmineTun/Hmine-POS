package hminepos.controller;

import hminepos.database.SqliteHelper;
import hminepos.helper.ItemEvent;
import hminepos.helper.ItemQuantity;
import hminepos.helper.ItemQuantityListener;
import hminepos.helper.Utils;
import hminepos.model.CustomerModel;
import hminepos.model.ProductModel;
import hminepos.model.SalesModel;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import org.controlsfx.control.SearchableComboBox;
import org.sqlite.util.StringUtils;

import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * Created by Mao on 12/17/2022.
 */


public class SalesController implements Initializable {


    @FXML
    private SearchableComboBox<CustomerModel> cbCustomerId;

    @FXML
    private SearchableComboBox<ProductModel> cbProductId;

    @FXML
    private TableColumn<SalesModel, Double> colAmount;

    @FXML
    private TableColumn<SalesModel, Double> colPrice;

    @FXML
    private TableColumn<SalesModel, String> colProductId;

    @FXML
    private TableColumn<SalesModel, String> colProductName;

    @FXML
    private TableColumn<SalesModel, Integer> colQuantity;

    @FXML
    private Button btSell;

    @FXML
    private VBox printableVoucher;

    @FXML
    private TableView<SalesModel> tableview;

    @FXML
    private TextField tfCustomerName;

    @FXML
    private TextField tfVoucherNo;

    @FXML
    private TextField vcCash;

    @FXML
    private Label vcChange;

    @FXML
    private Label vcTotalAmount;

    @FXML
    private Label vcTotalQuantity;

    @FXML
    void handleCancel(ActionEvent event) {

    }

    @FXML
    void handleSell(ActionEvent event) throws ParseException {

        // Same data for all table items
        // createdAt, voucher
        String createdAt = LocalDateTime.now().toString();
        for (SalesModel sale :
                tableview.getItems()) {
            sale.setVoucher(tfVoucherNo.getText());
            sale.setCreatedAt(createdAt);
            sale.setCreatedBy(Utils.getCurrentUserId());
            sale.setCustomerId(cbCustomerId.getValue() == null ? "" : cbCustomerId.getValue().getCustomerId());
            System.out.println(sale);
            // #1 Subtract quantity from products table
            if (SqliteHelper.subtractProducts(sale))
                // #2 Add the record to the sales table
                SqliteHelper.addSales(sale);
        }
        // After finishing adding all items, close the connection!
        SqliteHelper.closeConnection();
        prepareToSellAgain();

    }

    private void prepareToSellAgain() throws ParseException {
        cbCustomerId.setValue(null);
        cbProductId.setValue(null);
        tfCustomerName.clear();
        tableview.getItems().clear();
        vcCash.setText("0");
        vcChange.setText("0");
        vcTotalAmount.setText("0");
        vcTotalQuantity.setText("0");
        tfVoucherNo.setText(getVoucherNumber());
        btSell.setDisable(true);
        // After finish selling one time, to get the remaining quantity of our products
        // We must refresh cbProductId once again!!!
        cbProductId.setItems(FXCollections.observableArrayList(SqliteHelper.getAllProducts()));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCustomers();
        initProducts();
        initVouchers();
        setupTableColumValue();
    }

    // This method includes Voucher TextField & Voucher Report
    private void initVouchers() {
        try {
            tfVoucherNo.setText(getVoucherNumber());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        TextFormatter<Double> doubleTextFormatter = new TextFormatter<>(
                new DoubleStringConverter(),
                0.0,
                change -> Pattern.matches("\\d*", change.getText()) ? change : null
        );
        vcCash.setTextFormatter(doubleTextFormatter);
        vcCash.textProperty().addListener(observable -> {
            vcCalculator();
        });
    }

    private void setupTableColumValue() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colQuantity.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<>() {
            public String toString(Integer object) {
                return object.toString();
            }

            public Integer fromString(String string) {
                if (Utils.isNumeric(string))
                    return Integer.parseInt(string);
                return 1;
            }
        }));
        colQuantity.setOnEditCommit(event -> {
            SalesModel tmp = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue() <= tmp.getMaxQuantity()) {
                tmp.setQuantity(event.getNewValue());
                tmp.setAmount(event.getNewValue() * tmp.getPrice());
                vcCalculator();
                tableview.refresh();
            } else {
                // Not enough items
                tmp.setQuantity(event.getOldValue());
                tableview.refresh();
            }
        });
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void vcCalculator() {
        vcTotalQuantity.setText("" + tableview.getItems().stream().mapToInt(SalesModel::getQuantity).sum());
        vcTotalAmount.setText("" + tableview.getItems().stream().mapToDouble(SalesModel::getAmount).sum());
        double change = (Double.parseDouble(vcCash.getText()) - Double.parseDouble(vcTotalAmount.getText()));
        vcChange.setText("" + change);
        if (change < 0) {
            // Cannot sell Things
            vcChange.setTextFill(Color.RED);
            btSell.setDisable(true);
        } else {
            // Safe to Sell Things
            vcChange.setTextFill(Color.GREEN);
            btSell.setDisable(false);
        }

    }

    private void initProducts() {
        cbProductId.setItems(FXCollections.observableArrayList(SqliteHelper.getAllProducts()));
        Callback<ListView<ProductModel>, ListCell<ProductModel>> productCellFactory = new Callback<>() {
            @Override
            public ListCell<ProductModel> call(ListView<ProductModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(ProductModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            setText(item.getProductId());
                        }
                    }
                };
            }
        };
        cbProductId.setCellFactory(productCellFactory);
        cbProductId.setButtonCell(productCellFactory.call(null));
        cbProductId.showingProperty().addListener((observable, hidden, showing) -> {
            if (hidden) {
                if (cbProductId.getValue() != null) {
                    ProductModel newProduct = cbProductId.getValue();
                    if (tableview.getItems().contains(newProduct)) {
                        SalesModel existingProduct = tableview.getItems().stream().filter(pm -> pm.getProductId().equals(newProduct.getProductId())).findFirst().orElse(null);
                        existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                        existingProduct.setAmount(existingProduct.getQuantity() * existingProduct.getPrice());
                        tableview.getSelectionModel().select(existingProduct);
                        tableview.refresh();
                    } else {
                        SalesModel newSalesProduct = createSalesModelObject(newProduct);
                        tableview.getItems().add(newSalesProduct);
                        tableview.getSelectionModel().select(newSalesProduct);
                    }
                    vcCalculator();
                }
            }
        });
    }

    private SalesModel createSalesModelObject(ProductModel product) {
        SalesModel salesModel = new SalesModel();
        if (cbCustomerId.getValue() != null)
            salesModel.setCustomerId(cbCustomerId.getValue().getCustomerId());
        salesModel.setProductId(product.getProductId());
        salesModel.setProductName(product.getProductName());
        salesModel.setQuantity(1);
        salesModel.setMaxQuantity(product.getQuantity());
        salesModel.setPrice(product.getSalePrice());
        salesModel.setAmount(salesModel.getQuantity() * salesModel.getPrice());
        return salesModel;
    }

    private void initCustomers() {
        cbCustomerId.getItems().add(null);
        cbCustomerId.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllCustomers()));
        Callback<ListView<CustomerModel>, ListCell<CustomerModel>> cellFactory = new Callback<>() {
            @Override
            public ListCell<CustomerModel> call(ListView<CustomerModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(CustomerModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
//                            setGraphic(null);
                            setText("Unknown");
                        } else {
                            setText(item.getCustomerId());
                        }
                    }
                };
            }
        };
        cbCustomerId.setCellFactory(cellFactory);
        cbCustomerId.setButtonCell(cellFactory.call(null));
        cbCustomerId.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (cbCustomerId.getValue() != null)
                tfCustomerName.setText(cbCustomerId.getValue().getCustomerName());
            else
                tfCustomerName.clear();
        });
    }

    private String getVoucherNumber() throws ParseException {
        // Detect from Database here
        String theLastVoucherFromDatabase = SqliteHelper.getTheLastVoucher();
        boolean isToday = Utils.isVoucherToday(theLastVoucherFromDatabase);
        if (isToday) {
            int voucherNumber = Utils.extractVoucherNumber(theLastVoucherFromDatabase);
            return "s" + String.format("%04d", voucherNumber + 1) + "-" + Utils.getTodayDate();
        } else {
            return "s" + String.format("%04d", 1) + "-" + Utils.getTodayDate();
        }
    }

    public void handleQuantity(ActionEvent actionEvent) {
        int tableIndex = tableview.getSelectionModel().getSelectedIndex();
        if (tableIndex < 0) return;
        String itemName = tableview.getItems().get(tableIndex).getProductName();
        ItemQuantity.getInstance().setItemActionListener(itemName, tableIndex, itemEvent -> {
            if (itemEvent.getValue() <= tableview.getItems().get(itemEvent.getIndex()).getMaxQuantity()) {
                tableview.getItems().get(itemEvent.getIndex()).setQuantity(itemEvent.getValue());
            }
            tableview.refresh();
        });
    }

    public void handleDeleteItem(ActionEvent actionEvent) {
        if (!tableview.getItems().isEmpty()) {
            // Selected Index will never be NULL
            tableview.getItems().remove(tableview.getSelectionModel().getSelectedIndex());
        }
    }
}
