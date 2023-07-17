package hminepos.database;

import hminepos.helper.Type;
import hminepos.helper.Utils;
import hminepos.model.*;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mao on 12/2/2022.
 */


public class SqliteHelper {

    private static Connection con;

    private static void getConnection() throws SQLException, ClassNotFoundException {

        // sqlite driver
        Class.forName("org.sqlite.JDBC");
        // database path, Add
        con = DriverManager.getConnection("jdbc:sqlite:" + System.getProperty("user.dir") + "\\app\\HminePOS.db");

    }

    /*
    Started : User SCOPE
     */
    public static List<UserModel> getAllUsers() {
        List<UserModel> allUsers = new ArrayList<>();
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            String sql = "SELECT * FROM users;";
            PreparedStatement prep = con.prepareStatement(sql);
            ResultSet res = prep.executeQuery();
            while (res.next()) {
                UserModel user = new UserModel();
                user.setUserId(res.getString("user_id"));
                user.setUserName(res.getString("user_name"));
                user.setPhone(res.getString("phone"));
                user.setEmail(res.getString("email"));
                user.setPassword(res.getString("password"));
                user.setImage(res.getString("image"));
                allUsers.add(user);
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
        return allUsers;
    }

    public static boolean addUser(UserModel user) {

        String sql = "INSERT INTO users (user_id,user_name,phone,email,password,image) VALUES(?,?,?,?,?,?);";

        int addedRow = 0;
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, user.getUserId());
            pstmt.setString(2, user.getUserName());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPassword());
            pstmt.setString(6, user.getImage());
            addedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return addedRow > 0;
    }


    public static boolean updateUser(UserModel user) {
        String sql = "UPDATE users SET user_name=?,phone=?,email=?,image=? WHERE user_id=?";

        int updatedRow = 0;

        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, user.getUserName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getImage());
            pstmt.setString(5, user.getUserId());
            // update
            updatedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return updatedRow > 0;
    }

    public static UserModel getUserById(String userId) {
        UserModel user = null;
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            String sql = "SELECT * FROM users WHERE user_id = ?";
            PreparedStatement prep = con.prepareStatement(sql);
            prep.setString(1, userId);
            ResultSet res = prep.executeQuery();
            if (res.next()) {
                user = new UserModel();
                user.setUserId(res.getString("user_id"));
                user.setUserName(res.getString("user_name"));
                user.setPhone(res.getString("phone"));
                user.setEmail(res.getString("email"));
                user.setPassword(res.getString("password"));
                user.setImage(res.getString("image"));
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return user;
    }

    /////// Ended : User SCOPE

    /* Customer SCOPE */

    public static List<CustomerModel> getAllCustomers() {
        List<CustomerModel> allCustomers = new ArrayList<>();
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            String sql = "SELECT * FROM customers;";
            PreparedStatement prep = con.prepareStatement(sql);
            ResultSet res = prep.executeQuery();
            while (res.next()) {
                CustomerModel customer = new CustomerModel();
                customer.setCustomerId(res.getString("customer_id"));
                customer.setCustomerName(res.getString("customer_name"));
                customer.setPhone(res.getString("phone"));
                customer.setEmail(res.getString("email"));
                customer.setAddress(res.getString("address"));
                customer.setImage(res.getString("image"));
                customer.setCreatedBy(res.getString("created_by"));
                customer.setCreatedAt(res.getString("created_at"));
                allCustomers.add(customer);
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
        return allCustomers;
    }

    public static boolean addCustomer(CustomerModel customer) {

        String sql = "INSERT INTO customers (customer_id,customer_name,address,phone,email,image,created_by,created_at) VALUES(?,?,?,?,?,?,?,?);";

        int addedRow = 0;
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, customer.getCustomerId());
            pstmt.setString(2, customer.getCustomerName());
            pstmt.setString(3, customer.getAddress());
            pstmt.setString(4, customer.getPhone());
            pstmt.setString(5, customer.getEmail());
            pstmt.setString(6, customer.getImage());
            pstmt.setString(7, customer.getCreatedBy());
            pstmt.setString(8, customer.getCreatedAt());
            addedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return addedRow > 0;
    }

    public static boolean updateCustomer(CustomerModel customer) {
        String sql = "UPDATE customers SET customer_name=?,address=?,phone=?,email=?,image=? WHERE customer_id=?";

        int updatedRow = 0;

        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setString(2, customer.getAddress());
            pstmt.setString(3, customer.getPhone());
            pstmt.setString(4, customer.getEmail());
            pstmt.setString(5, customer.getImage());
            pstmt.setString(6, customer.getCustomerId());
            // update
            updatedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return updatedRow > 0;
    }



    /////////// END Customer SCOPE /////////////////

    /* STARTED Supplier Scope */
    public static List<SupplierModel> getAllSuppliers() {
        List<SupplierModel> allSuppliers = new ArrayList<>();
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            String sql = "SELECT * FROM suppliers;";
            PreparedStatement prep = con.prepareStatement(sql);
            ResultSet res = prep.executeQuery();
            while (res.next()) {
                SupplierModel supplier = new SupplierModel();
                supplier.setSupplierId(res.getString("supplier_id"));
                supplier.setSupplierName(res.getString("supplier_name"));
                supplier.setAddress(res.getString("address"));
                supplier.setPhone(res.getString("phone"));
                supplier.setEmail(res.getString("email"));
                supplier.setImage(res.getString("image"));
                supplier.setCreatedBy(res.getString("created_by"));
                supplier.setCreatedAt(res.getString("created_at"));
                allSuppliers.add(supplier);
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
        return allSuppliers;
    }

    public static boolean addSupplier(SupplierModel supplier) {

        String sql = "INSERT INTO suppliers (supplier_id,supplier_name,address,phone,email,image,created_by,created_at) VALUES(?,?,?,?,?,?,?,?);";

        int addedRow = 0;
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, supplier.getSupplierId());
            pstmt.setString(2, supplier.getSupplierName());
            pstmt.setString(3, supplier.getAddress());
            pstmt.setString(4, supplier.getPhone());
            pstmt.setString(5, supplier.getEmail());
            pstmt.setString(6, supplier.getImage());
            pstmt.setString(7, supplier.getCreatedBy());
            pstmt.setString(8, supplier.getCreatedAt());
            addedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return addedRow > 0;
    }

    public static boolean updateSupplier(SupplierModel supplier) {
        String sql = "UPDATE suppliers SET supplier_name=?,address=?,phone=?,email=?,image=? WHERE supplier_id=?";

        int updatedRow = 0;

        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, supplier.getSupplierName());
            pstmt.setString(2, supplier.getAddress());
            pstmt.setString(3, supplier.getPhone());
            pstmt.setString(4, supplier.getEmail());
            pstmt.setString(5, supplier.getImage());
            pstmt.setString(6, supplier.getSupplierId());
            // update
            updatedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return updatedRow > 0;
    }


    // STARTED PRODUCT SCOPE //
    public static List<ProductModel> getAllProducts() {
        List<ProductModel> allProducts = new ArrayList<>();
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            String sql = "SELECT * FROM products;";
            PreparedStatement prep = con.prepareStatement(sql);
            ResultSet res = prep.executeQuery();
            while (res.next()) {
                ProductModel product = new ProductModel();
                product.setProductId(res.getString("product_id"));
                product.setProductName(res.getString("product_name"));
                product.setQuantity(res.getInt("quantity"));
                product.setPurchasePrice(res.getDouble("purchase_price"));
                product.setSalePrice(res.getDouble("sale_price"));
                product.setImage(res.getString("image"));
                product.setCreatedBy(res.getString("created_by"));
                product.setCreatedAt(res.getString("created_at"));
                allProducts.add(product);
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
        return allProducts;
    }

    public static boolean addProduct(ProductModel product) {

        String sql = "INSERT INTO products (product_id,product_name,quantity,purchase_price,sale_price,image,created_by,created_at) VALUES(?,?,?,?,?,?,?,?);";

        int addedRow = 0;
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, product.getProductId());
            pstmt.setString(2, product.getProductName());
            pstmt.setInt(3, product.getQuantity());
            pstmt.setDouble(4, product.getPurchasePrice());
            pstmt.setDouble(5, product.getSalePrice());
            pstmt.setString(6, product.getImage());
            pstmt.setString(7, product.getCreatedBy());
            pstmt.setString(8, product.getCreatedAt());
            addedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return addedRow > 0;
    }

    public static boolean updateProduct(ProductModel product) {
        String sql = "UPDATE products SET product_name=?,quantity=?,purchase_price=?,sale_price=?,image=? WHERE product_id=?";

        int updatedRow = 0;

        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            // set the corresponding param
            pstmt.setString(1, product.getProductName());
            pstmt.setInt(2, product.getQuantity());
            pstmt.setDouble(3, product.getPurchasePrice());
            pstmt.setDouble(4, product.getSalePrice());
            pstmt.setString(5, product.getImage());
            pstmt.setString(6, product.getProductId());
            // update
            updatedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return updatedRow > 0;
    }

    // Sales SCOPE!!!
    public static String getTheLastVoucher(Type type) {
        String str = "";
        if (type == Type.SALE) {
            str = "sales";
        } else {
            str = "purchases";
        }
        String query = "SELECT voucher FROM " + str + " ORDER BY no DESC LIMIT 1;";
        // Initialize with today voucher number!
        // Given 0 here will increase to 1 in getVoucherNumber method from Sales & Purchases
        String result = str.charAt(0) + String.format("%04d", 0) + "-" + Utils.getTodayDate();
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement prep = con.prepareStatement(query);
            ResultSet res = prep.executeQuery();
            if (res.next()) {
                result = res.getString("voucher");
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static boolean addSales(SalesModel sale) {

        String sql = "INSERT INTO sales (voucher,product_id,customer_id,quantity,price,created_by,created_at) VALUES(?,?,?,?,?,?,?);";

        int addedRow = 0;
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, sale.getVoucher());
            pstmt.setString(2, sale.getProductId());
            pstmt.setString(3, sale.getCustomerId());
            pstmt.setInt(4, sale.getQuantity());
            pstmt.setDouble(5, sale.getPrice());
            pstmt.setString(6, sale.getCreatedBy());
            pstmt.setString(7, sale.getCreatedAt());
            addedRow = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return addedRow > 0;
    }

    public static void closeConnection() {
        try {
            if (!con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean subtractProducts(SalesModel sale) {
        String sql = "UPDATE products SET quantity=? WHERE product_id=?";
        int updatedRow = 0;

        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            // set the corresponding param
            pstmt.setInt(1, (sale.getMaxQuantity() - sale.getQuantity()));
            pstmt.setString(2, sale.getProductId());
            // update
            updatedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return updatedRow > 0;
    }

    public static boolean increaseProduct(PurchasesModel purchase) {
        String sql = "UPDATE products SET quantity=? WHERE product_id=?";
        int updatedRow = 0;

        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            // set the corresponding param
            // MaxQuantity is Current Quantity you have
            // Quantity is the amount you are going to increase
            pstmt.setInt(1, (purchase.getMaxQuantity() + purchase.getQuantity()));
            pstmt.setString(2, purchase.getProductId());
            // update
            updatedRow = pstmt.executeUpdate();
            pstmt.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return updatedRow > 0;
    }

    public static boolean addPurchase(PurchasesModel purchase) {

        String sql = "INSERT INTO purchases (voucher,product_id,supplier_id,quantity,price,created_by,created_at) VALUES(?,?,?,?,?,?,?);";

        int addedRow = 0;
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, purchase.getVoucher());
            pstmt.setString(2, purchase.getProductId());
            pstmt.setString(3, purchase.getSupplierId());
            pstmt.setInt(4, purchase.getQuantity());
            pstmt.setDouble(5, purchase.getPrice());
            pstmt.setString(6, purchase.getCreatedBy());
            pstmt.setString(7, purchase.getCreatedAt());
            addedRow = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return addedRow > 0;
    }

    public static List<SalesModel> getSalesData(String strStart, String strEnd) {
        List<SalesModel> salesModels = new ArrayList<>();
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            String sql = "SELECT * FROM sales WHERE created_at > ? AND created_at < ?;";
            PreparedStatement prep = con.prepareStatement(sql);
            prep.setString(1, strStart);
            prep.setString(2, strEnd);
            ResultSet res = prep.executeQuery();
            for (int i = 1; res.next(); i ++) {
                SalesModel salesModel = new SalesModel();
                salesModel.setNo(i);
                salesModel.setVoucher(res.getString("voucher"));
                salesModel.setCustomerId(res.getString("customer_id"));
                salesModel.setProductId(res.getString("product_id"));
                salesModel.setPrice(res.getDouble("price"));
                salesModel.setQuantity(res.getInt("quantity"));
                salesModel.setCreatedBy(res.getString("created_by"));
                salesModel.setCreatedAt(res.getString("created_at"));
                salesModels.add(salesModel);
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
        return salesModels;
    }

    public static List<PurchasesModel> getPurchaseData(String strStart, String strEnd) {
        List<PurchasesModel> purchasesModels = new ArrayList<>();
        try {
            if (con == null || con.isClosed()) {
                getConnection();
            }
            String sql = "SELECT * FROM purchases WHERE created_at > ? AND created_at < ?;";
            PreparedStatement prep = con.prepareStatement(sql);
            prep.setString(1, strStart);
            prep.setString(2, strEnd);
            ResultSet res = prep.executeQuery();
            for (int i = 1; res.next(); i ++) {
                PurchasesModel purchasesModel = new PurchasesModel();
                purchasesModel.setNo(i);
                purchasesModel.setVoucher(res.getString("voucher"));
                purchasesModel.setSupplierId(res.getString("supplier_id"));
                purchasesModel.setProductId(res.getString("product_id"));
                purchasesModel.setPrice(res.getDouble("price"));
                purchasesModel.setQuantity(res.getInt("quantity"));
                purchasesModel.setCreatedBy(res.getString("created_by"));
                purchasesModel.setCreatedAt(res.getString("created_at"));
                purchasesModels.add(purchasesModel);
            }
            res.close();
            prep.close();
            con.close();
        } catch (SQLException | ClassNotFoundException throwable) {
            throwable.printStackTrace();
        }
        return purchasesModels;
    }
}
