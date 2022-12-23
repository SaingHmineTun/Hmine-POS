package hminepos.helper;

import javafx.stage.Stage;

/**
 * Created by Mao on 12/23/2022.
 */



public class PurchaseConfirmEvent {
    private boolean isConfirm;
    private Stage stage;
    private double amount, pay, change;

    public double getAmount() {
        return amount;
    }

    public void setConfirm(boolean confirm) {
        isConfirm = confirm;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public PurchaseConfirmEvent(double amount, double pay, double change) {
        this.amount = amount;
        this.pay = pay;
        this.change = change;
    }

    public void setChangedPrice(double amount, double pay, double change) {
        setAmount(amount);
        setPay(pay);
        setChange(change);
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public PurchaseConfirmEvent(boolean confirm, Stage stage) {
        this.isConfirm = confirm;
        this.stage = stage;
    }

    public Stage getConfirmBox() {
        return stage;
    }

    public boolean isConfirm() {
        return isConfirm;
    }
}
