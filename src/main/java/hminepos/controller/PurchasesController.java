package hminepos.controller;

import hminepos.database.SqliteHelper;
import hminepos.helper.ItemQuantity;
import hminepos.helper.Utils;
import hminepos.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.controlsfx.control.SearchableComboBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Mao on 12/23/2022.
 */


public class PurchasesController implements Initializable {

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnReceive;

    @FXML
    private SearchableComboBox<ProductModel> cbProductId;

    @FXML
    private SearchableComboBox<SupplierModel> cbSupplierId;

    @FXML
    private TableColumn<PurchasesModel, Double> colAmount;

    @FXML
    private TableColumn<PurchasesModel, Double> colPrice;

    @FXML
    private TableColumn<PurchasesModel, String> colProductID;

    @FXML
    private TableColumn<PurchasesModel, String> colProductName;

    @FXML
    private TableColumn<PurchasesModel, Integer> colQuantity;

    @FXML
    private TableView<PurchasesModel> tableview;

    @FXML
    private TextField tfSupplierName;

    @FXML
    private TextField tfTotalAmount;

    @FXML
    private TextField tfTotalQuantity;

    @FXML
    private TextField tfVoucher;

    @FXML
    void actionVoucher(ActionEvent event) {

    }

    @FXML
    void handleDeleteItem(ActionEvent event) {
        if (!tableview.getItems().isEmpty()) {
            // Selected Index will never be NULL
            tableview.getItems().remove(tableview.getSelectionModel().getSelectedIndex());
        }
    }

    @FXML
    void handleItemQuantity(ActionEvent event) {
        int tableIndex = tableview.getSelectionModel().getSelectedIndex();
        if (tableIndex < 0) return;
        String itemName = tableview.getItems().get(tableIndex).getProductName();
        ItemQuantity.getInstance().setItemActionListener(itemName, tableIndex, itemEvent -> {
            if (itemEvent.getValue() <= tableview.getItems().get(itemEvent.getIndex()).getMaxQuantity()) {
                tableview.getItems().get(itemEvent.getIndex()).setQuantity(itemEvent.getValue());
            }
            tableview.refresh();
            vcCalculator();
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSuppliers();
        initProducts();
        setupTableColumnValueFactory();
    }

    private void setupTableColumnValueFactory() {
        colProductID.setCellValueFactory(new PropertyValueFactory<>("productId"));
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
            PurchasesModel tmp = event.getTableView().getItems().get(event.getTablePosition().getRow());
            if (event.getNewValue() <= tmp.getMaxQuantity()) {
                tmp.setQuantity(event.getNewValue());
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
                        PurchasesModel existingProduct = tableview.getItems().stream().filter(pm -> pm.getProductId().equals(newProduct.getProductId())).findFirst().orElse(null);
                        existingProduct.setQuantity(existingProduct.getQuantity() + 1);
                        existingProduct.setAmount(existingProduct.getQuantity() * existingProduct.getPrice());
                        tableview.getSelectionModel().select(existingProduct);
                        tableview.refresh();
                    } else {
                        PurchasesModel newPurchasesModel = createPurchasesModelObject(newProduct);
                        tableview.getItems().add(newPurchasesModel);
                        tableview.getSelectionModel().select(newPurchasesModel);
                    }
                    vcCalculator();
                }
                System.out.println("Handle Product ID");
            }
        });
    }

    private void vcCalculator() {
        tfTotalQuantity.setText("" + tableview.getItems().stream().mapToInt(PurchasesModel::getQuantity).sum());
        tfTotalAmount.setText("" + tableview.getItems().stream().mapToDouble(PurchasesModel::getAmount).sum());
    }

    private PurchasesModel createPurchasesModelObject(ProductModel product) {
        PurchasesModel purchasesModel = new PurchasesModel();
        if (cbSupplierId.getValue() != null)
            purchasesModel.setSupplierId(cbSupplierId.getValue().getSupplierId());
        purchasesModel.setProductId(product.getProductId());
        purchasesModel.setProductName(product.getProductName());
        purchasesModel.setQuantity(1);
        purchasesModel.setMaxQuantity(product.getQuantity());
        purchasesModel.setPrice(product.getPurchasePrice());
        purchasesModel.setAmount(purchasesModel.getQuantity() * purchasesModel.getPrice());
        return purchasesModel;
    }


    private void initSuppliers() {
        cbSupplierId.getItems().add(null);
        cbSupplierId.getItems().addAll(FXCollections.observableArrayList(SqliteHelper.getAllSuppliers()));
        Callback<ListView<SupplierModel>, ListCell<SupplierModel>> cellFactory = new Callback<>() {
            @Override
            public ListCell<SupplierModel> call(ListView<SupplierModel> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(SupplierModel item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty) {
//                            setGraphic(null);
                            setText("Unknown");
                        } else {
                            setText(item.getSupplierId());
                        }
                    }
                };
            }
        };
        cbSupplierId.setCellFactory(cellFactory);
        cbSupplierId.setButtonCell(cellFactory.call(null));
        cbSupplierId.showingProperty().addListener((observable, hidden, showing) -> {
            if (hidden) {
                if (cbSupplierId.getValue() != null)
                    tfSupplierName.setText(cbSupplierId.getValue().getSupplierName());
                else
                    tfSupplierName.clear();
                System.out.println("Handle Supplier ID");
            }
        });
    }

}
