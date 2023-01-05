package hminepos.model;

import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Created by Mao on 12/16/2022.
 */

@BsonDiscriminator
public class SupplierModel {

    @BsonProperty("supplier_id")
    private String supplierId;
    @BsonProperty("supplier_name")
    private String supplierName;
    @BsonProperty("address")
    private String address;
    @BsonProperty("phone")
    private String phone;
    @BsonProperty("email")
    private String email;
    @BsonProperty("image")
    private String image;
    @BsonProperty("created_by")
    private String createdBy;
    @BsonProperty("created_at")
    private String createdAt;

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public SupplierModel() {
    }

    public SupplierModel(String supplierId, String supplierName, String address, String phone, String email, String image, String createdBy, String createdAt) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return this.supplierId;
    }
}
