package hminepos.model;

/**
 * Created by Mao on 12/16/2022.
 */


public class ProductModel {
    private String productId;
    private String productName;
    private int quantity;
    private double purchasePrice;
    private double salePrice;
    private String image;
    private String createdBy;
    private String createdAt;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
    public boolean equals(Object obj) {
        if (obj instanceof ProductModel) {
            return ((ProductModel)obj).getProductId().equals(getProductId());
        } else if (obj instanceof SalesModel) {
            return ((SalesModel)obj).getProductId().equals(getProductId());
        } return false;
    }
}
