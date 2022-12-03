package hminepos.database;

import hminepos.model.UserModel;

import java.sql.*;
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
        // database path, if it's a new database
        con = DriverManager.getConnection("jdbc:sqlite::resource:HminePOS.db");

    }

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


}
