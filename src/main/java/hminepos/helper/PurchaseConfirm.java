package hminepos.helper;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by SaiMao on 6/28/2017.
 */
public class PurchaseConfirm {


    @FXML
    private Label lbAmount;

    @FXML
    private Label lbChange;

    @FXML
    private TextField tfPay;

    @FXML
    private JFXButton btConfirm;

    private PurchaseConfirmListener listener;
    private PurchaseConfirmEvent eventConfirm, eventCancel;

    private Stage stage;


    public PurchaseConfirm(double amountTotal) {
        try {
            showView(amountTotal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showView(double amountValue) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/purchase-confirmation.fxml"));
        stage = new Stage();
        loader.setController(this);
        VBox vBox = loader.load();
        Scene scene = new Scene(vBox);

        lbAmount.setText(amountValue + "");
        lbChange.setText(amountValue - (tfPay.getText().isEmpty() || !Utils.isNumeric(tfPay.getText()) ? 0 : Double.parseDouble(tfPay.getText())) + "");

        TextFormatter<Double> formatter = new TextFormatter<>(new DoubleStringConverter(), 0.0, change1 -> Pattern.matches("\\d*", change1.getText()) ? change1 : null);
        tfPay.setTextFormatter(formatter);
        tfPay.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty() && Utils.isNumeric(newValue))
                setChange(Double.parseDouble(newValue));
        });

        tfPay.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER && !btConfirm.isDisabled()) {
                btConfirm.fire();
            }
        });

        eventConfirm = new PurchaseConfirmEvent(Double.parseDouble(lbAmount.getText()), Double.parseDouble(tfPay.getText()), Double.parseDouble(lbChange.getText()));

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleCancel(ActionEvent event) {
        eventCancel = new PurchaseConfirmEvent(false, stage);
        listener.purchaseConfirmed(eventCancel);
    }


    @FXML
    void handleConfirm(ActionEvent event) {
        eventConfirm.setConfirm(true);
        eventConfirm.setStage(stage);
        listener.purchaseConfirmed(eventConfirm);
    }

    private void setChange(double payValue) {
        double amountValue = Double.parseDouble(lbAmount.getText());
        double changeValue = Double.parseDouble(lbChange.getText());
        try {
            double amtChange = payValue - amountValue;
            lbChange.setText(amtChange + "");
            eventConfirm.setChangedPrice(amountValue, payValue, changeValue);
            if (amtChange >= 0) {
                lbChange.setTextFill(Color.GREEN);
                btConfirm.setDisable(false);
            } else {
                lbChange.setTextFill(Color.RED);
                btConfirm.setDisable(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnPurchaseConfirmListener(PurchaseConfirmListener listener) {
        this.listener = listener;
    }

}


