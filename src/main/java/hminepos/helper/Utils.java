package hminepos.helper;

import com.password4j.Password;
import hminepos.model.UserModel;
import javafx.scene.image.Image;

/**
 * Created by Mao on 12/1/2022.
 */


public class Utils {

    private static UserModel currentUser;

    public static UserModel getCurrentUser() {
        return currentUser;
    }

    public static String getCurrentUserId() {
        return currentUser.getUserId();
    }

    public static String getCurrentUserName() {
        return currentUser.getUserName();
    }

    public static void setCurrentUser(UserModel currentUser) {
        Utils.currentUser = currentUser;
    }

    public static boolean checkPassword(String hashPassword, String yourPassword) {

        return Password.check(yourPassword, hashPassword).withArgon2();
    }
    public static Image getDefaultUserImage() {
        return new Image(Utils.class.getResource("/images/user.png").toExternalForm());
    }

    public static Image getDefaultProductImage() {
        return new Image(Utils.class.getResource("/images/item.png").toExternalForm());
    }

}
