package hminepos.controller;

import hminepos.helper.CurrentTime;
import hminepos.helper.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class FooterController implements Initializable {

    @FXML
    private Label userName;

    @FXML
    private Label date;

    @FXML
    private Label user;

    @FXML
    private HBox time;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userName.setText(Utils.getCurrentUserName());
        date.setText(Utils.getTodayDate());
        time.getChildren().add(new CurrentTime());
    }
}
