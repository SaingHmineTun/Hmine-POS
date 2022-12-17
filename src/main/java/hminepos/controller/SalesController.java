package hminepos.controller;

import hminepos.database.SqliteHelper;
import hminepos.model.CustomerModel;
import hminepos.model.ProductModel;
import hminepos.model.SalesModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

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
    private Button handleDeleteItem;

    @FXML
    private Button handleQuantity;

    @FXML
    private VBox printableVoucher;

    @FXML
    private TableView<SalesModel> tableview;

    @FXML
    private TextField tfCustomerName;

    @FXML
    private TextField tfTotalAmount;

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
    void handleSell(ActionEvent event) {

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCustomers();
        initProducts();
    }

    private void initProducts() {
        cbProductId.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllProducts()));
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
        cbProductId.getSelectionModel().selectedItemProperty().addListener(observable -> {
            if (cbProductId.getValue() != null) {
                // TODO: Set Table Values!!!
            }
        });
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
                            setGraphic(null);
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
}
