package hminepos.helper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

/**
 * Created by SaiMao on 5/12/2017.
 */
public class ItemQuantity {
    @FXML
    private TextField input;

    @FXML
    private Button btnCancel;

    @FXML
    private Label quantity;

    @FXML
    private Button btnOK;


    ItemQuantityListener listener;


    private Stage stage;
    private String itemName;
    private int index;
    private static final ItemQuantity itemQuantity = new ItemQuantity();


    public void setItemActionListener(String itemName, int index, ItemQuantityListener listener) {
        this.index = index;
        this.itemName = itemName;
        this.listener = listener;
        showPrompt();
    }

    public static ItemQuantity getInstance() {
        return itemQuantity;
    }

    public void fireEvent(ItemEvent e) {
        listener.setItemQuantity(e);
    }

    private void buttonClick() {
        btnCancel.setOnAction(event -> stage.close());
        btnOK.setOnAction(event -> {
            fireEvent(new ItemEvent(index, (Integer.parseInt(input.getText()))));
            stage.close();
        });

        input.setTextFormatter(new TextFormatter<>(
                new IntegerStringConverter(),
                0,
                new IntegerFilter())
        );

        input.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                listener.setItemQuantity(new ItemEvent(index, (Integer.parseInt(input.getText()))));
                stage.close();
            }
        });
    }

    private ItemQuantity() {

    }

    public void showPrompt() {
        stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/item-quantity.fxml"));
        loader.setController(this);
        Parent parent = null;
        try {
            parent = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        quantity.setText(itemName);
        buttonClick();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
