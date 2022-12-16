package hminepos.controller;

import com.jfoenix.controls.JFXButton;
import hminepos.database.SqliteHelper;
import hminepos.helper.ImageEncoder;
import hminepos.helper.ImageResizer;
import hminepos.helper.Utils;
import hminepos.model.ProductModel;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Created by Mao on 12/16/2022.
 */


public class ProductController implements Initializable {
    @FXML
    private JFXButton btAddProduct;

    @FXML
    private JFXButton btArtistDelete;

    @FXML
    private TableColumn<ProductModel, String> colProductId;

    @FXML
    private TableColumn<ProductModel, String> colProductName;

    @FXML
    private TableColumn<ProductModel, Double> colPurchasePrice;

    @FXML
    private TableColumn<ProductModel, Integer> colQuantity;

    @FXML
    private TableColumn<ProductModel, Double> colSalePrice;

    @FXML
    private TextField filterById;

    @FXML
    private TextField filterByName;

    @FXML
    private ImageView ivPicture;

    @FXML
    private BorderPane root;

    @FXML
    private TableView<ProductModel> tableProducts;

    @FXML
    private MFXTextField tfProductId;

    @FXML
    private MFXTextField tfProductName;

    @FXML
    private MFXTextField tfPurchasePrice;

    @FXML
    private MFXTextField tfQuantity;

    @FXML
    private MFXTextField tfSalePrice;

    @FXML
    private HBox updateLayout;

    private ProductModel selectedProduct;

    private boolean isUpdatedPicture;

    private ObservableList<ProductModel> allProducts;

    @FXML
    void handleAddNewProduct(ActionEvent event) {

        tfProductId.clear();
        tfProductName.clear();
        tfQuantity.clear();
        tfPurchasePrice.clear();
        tfSalePrice.clear();
        ivPicture.setImage(Utils.getDefaultProductImage());
        hideUpdateLayout(true);
        tableProducts.getSelectionModel().clearSelection();
    }

    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {

        // Safe to register!!!
        ProductModel product = new ProductModel();
        product.setProductId(tfProductId.getText());
        product.setProductName(tfProductName.getText());
        // Only numbers must be allowed!!!
        product.setQuantity(Integer.parseInt(tfQuantity.getText()));
        product.setPurchasePrice(Double.parseDouble(tfPurchasePrice.getText()));
        product.setSalePrice(Double.parseDouble(tfSalePrice.getText()));
        // Converting an image file to string
        if (!ivPicture.getImage().getUrl().endsWith("images/user.png")) {
            // Resize image to 128w/128h
            product.setImage(resizeImage());
        }
        // Created By
        product.setCreatedBy(Utils.getCurrentUserId());
        // Created At
        product.setCreatedAt(LocalDateTime.now().toString());

        /*
        SupplierModel object is ready! Let's add to the database!!!
         */
        SqliteHelper.addProduct(product);
        refreshTable();
    }

    private void refreshTable() {
        allProducts = FXCollections.observableArrayList(SqliteHelper.getAllProducts());
        tableProducts.setItems(allProducts);
    }

    @FXML
    void handleClearFilter(ActionEvent event) {

        filterById.clear();
        filterByName.clear();

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
    void handleUpdateProduct(ActionEvent event) throws IOException {
        ProductModel product = new ProductModel();
        product.setProductId(tfProductId.getText());
        product.setProductName(tfProductName.getText());
        product.setQuantity(Integer.parseInt(tfQuantity.getText()));
        product.setPurchasePrice(Double.parseDouble(tfPurchasePrice.getText()));
        product.setSalePrice(Double.parseDouble(tfSalePrice.getText()));
        if (isUpdatedPicture) {
            product.setImage(resizeImage());
        } else {
            product.setImage(selectedProduct.getImage());
        }
        if (isUpdatedPicture || validForUpdate(product)) {
            // Ready to update into database
            SqliteHelper.updateProduct(product);
            refreshTable();
        }
    }

    private boolean validForUpdate(ProductModel product) {
        return !selectedProduct.getProductName().equals(product.getProductName())
                || selectedProduct.getQuantity() == product.getQuantity()
                || selectedProduct.getPurchasePrice() == product.getPurchasePrice()
                || selectedProduct.getSalePrice() == product.getSalePrice();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hideUpdateLayout(true);
        setupTableColumValueFactory();
        setupSelectTableRow();
        setupTableFiltering();

        allProducts = FXCollections.observableArrayList(SqliteHelper.getAllProducts());
        tableProducts.setItems(allProducts);
    }

    private void setupTableFiltering() {
        filterById.textProperty().addListener(observable -> filterTableProducts());
        filterByName.textProperty().addListener(observable -> filterTableProducts());
    }

    private void filterTableProducts() {
        List<ProductModel> filteredList = allProducts;

        if (!filterById.getText().isEmpty())
            filteredList = filteredList.stream().filter(product -> product.getProductId().contains(filterById.getText())).collect(Collectors.toList());

        if (!filterByName.getText().isEmpty())
            filteredList = filteredList.stream().filter(product -> product.getProductName().contains(filterByName.getText())).collect(Collectors.toList());

        tableProducts.setItems(FXCollections.observableArrayList(filteredList));
        if (filteredList.size() > 0) tableProducts.getSelectionModel().selectFirst();
    }


    private void setupSelectTableRow() {
        tableProducts.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue == null) return;
            selectedProduct = newValue;
            isUpdatedPicture = false;
            tfProductId.setText(selectedProduct.getProductId());
            tfProductName.setText(selectedProduct.getProductName());
            tfQuantity.setText("" + selectedProduct.getQuantity());
            tfPurchasePrice.setText("" + selectedProduct.getPurchasePrice());
            tfSalePrice.setText("" + selectedProduct.getSalePrice());
            if (selectedProduct.getImage() != null && selectedProduct.getImage().length() > 0) {
                ivPicture.setImage(ImageEncoder.decodeToImage(selectedProduct.getImage()));
            } else {
                ivPicture.setImage(Utils.getDefaultProductImage());
            }
            hideUpdateLayout(false);
        });
    }

    private void setupTableColumValueFactory() {
        colProductId.setCellValueFactory(new PropertyValueFactory<>("productId"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colPurchasePrice.setCellValueFactory(new PropertyValueFactory<>("purchasePrice"));
        colSalePrice.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
    }

    private void hideUpdateLayout(boolean bool) {

        if (bool) {
            // This is ADD NEW USER
            updateLayout.setVisible(false);
            updateLayout.setManaged(false);
            btAddProduct.setVisible(true);
            btAddProduct.setManaged(true);
            tfProductId.setEditable(true);

        } else {
            // This is UPDATE USER
            updateLayout.setVisible(true);
            updateLayout.setManaged(true);
            btAddProduct.setVisible(false);
            btAddProduct.setManaged(false);
            tfProductId.setEditable(false);
        }
    }

    private String resizeImage() throws IOException {
        String inputUrl = ivPicture.getImage().getUrl();
        String outputUrl = inputUrl.substring(0, inputUrl.lastIndexOf('.'));
        String type = inputUrl.substring(inputUrl.lastIndexOf(".") + 1);
        outputUrl = outputUrl + "_compressed." + type;
        ImageResizer.resize(inputUrl, outputUrl, 128, 128);
        String resizedImage = ImageEncoder.encodeToString(new Image(outputUrl), type);
        Files.deleteIfExists(Paths.get(outputUrl));
        return resizedImage;
    }


}
