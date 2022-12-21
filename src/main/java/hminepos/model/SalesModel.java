package hminepos.model;

/**
 * Created by Mao on 12/17/2022.
 */


public class SalesModel {
    private int no;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    private String voucher;
    private String productId;
    private String customerId;
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

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "SalesModel{" +
                "no=" + no +
                ", voucher='" + voucher + '\'' +
                ", productId='" + productId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", createdBy='" + createdBy + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", productName='" + productName + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ProductModel) {
            return ((ProductModel)obj).getProductId().equals(getProductId());
        } else if (obj instanceof SalesModel) {
            return ((SalesModel)obj).getProductId().equals(getProductId());
        } return false;
    }
}
