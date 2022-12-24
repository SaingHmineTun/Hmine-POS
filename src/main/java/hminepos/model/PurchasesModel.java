package hminepos.model;

/**
 * Created by Mao on 12/17/2022.
 */


public class PurchasesModel {
    private int no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    private String voucher;
    private String productId;
    private String supplierId;
    private int quantity;
    private double price;
    private String createdBy;
    private String createdAt;

    // Only necessary for showing data in tableview!!!
    private String productName;

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    private int maxQuantity;
    private double amount;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getSupplierId() {
        if (this.supplierId.isEmpty()) return "Unknown";
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        this.amount = this.price * quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        // Show Date Time in user-friendly style!
        if (!createdAt.isEmpty()) {
            String[] strings = createdAt.split("T");
            String date = strings[0];
            String time = strings[1].substring(0, strings[1].lastIndexOf("."));
            return date.concat(" ").concat(time);
        }
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PurchasesModel{" +
                "no=" + no +
                ", voucher='" + voucher + '\'' +
                ", productId='" + productId + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", productName='" + productName + '\'' +
                ", maxQuantity=" + maxQuantity +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductModel) {
            return ((ProductModel)obj).getProductId().equals(getProductId());
        } else if (obj instanceof PurchasesModel) {
            return ((PurchasesModel)obj).getProductId().equals(getProductId());
        } return false;
    }
}
