package hminepos.helper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by SaiMao on 5/18/2017.
 */
public class MessageBox {
    @FXML
    private ImageView alertType;

    @FXML
    private Label Message;

    @FXML
    private Button Ok;

    private Stage popup;
    private MessageBoxListener listener;
    VBox vBox;

    public static MessageBox messageBox = new MessageBox();

    private MessageBox() {
        popup = new Stage();
//        popup.getContent().add(popupContent());
        popup.setScene(new Scene(popupContent()));
        Message.setWrapText(true);
        Ok.setOnAction(event -> {
            if (listener != null) {
                listener.messageArrived(new MessageBoxEvent(getMessage()));
            }
            popup.close();
        });
        popup.initStyle(StageStyle.UNDECORATED);
        popup.initModality(Modality.APPLICATION_MODAL);
    }

    public MessageBox(String message) {
        this();
        setMessage(message);
    }

    public static MessageBox getInstance() {
        return messageBox;
    }

    public void setMessage(String message) {
        Message.setText(message);
    }

    public String getMessage() {
        return Message.getText();
    }

    public void setWrapText(boolean enable) {
        Message.setWrapText(enable);
    }

    public void setButtonText(String text) {
        Ok.setText(text);
    }

    public void setMessage(String message, Font font) {
        Message.setText(message);
        Message.setFont(font);
    }

    public void setMessageListener(MessageBoxListener listener) {
        this.listener = listener;
    }

    public void show() {
        popup.show();
        getButton().setDefaultButton(true);
    }

    public void showAndWait() {
        popup.showAndWait();
    }

    private VBox popupContent() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/message-box.fxml"));
            loader.setController(this);
            vBox = loader.load();
            vBox.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
            getButton().setDefaultButton(true);

        } catch (IOException e) {

        }
        return vBox;
    }

    public Button getButton() {
        return Ok;
    }

    public Stage getWindow() {
        return popup;
    }

}
