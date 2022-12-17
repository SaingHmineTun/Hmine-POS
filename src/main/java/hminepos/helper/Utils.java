package hminepos.helper;

import com.password4j.Password;
import hminepos.model.UserModel;
import javafx.scene.image.Image;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

    public static String resizeImage(String inputUrl) throws IOException {
        String outputUrl = inputUrl.substring(0, inputUrl.lastIndexOf('.'));
        String type = inputUrl.substring(inputUrl.lastIndexOf(".") + 1);
        outputUrl = outputUrl + "_compressed." + type;
        ImageResizer.resize(inputUrl, outputUrl, 128, 128);
        String resizedImage = ImageEncoder.encodeToString(new Image(outputUrl), type);
        Files.deleteIfExists(Paths.get(outputUrl));
        return resizedImage;
    }


    public static String getTodayDate() {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String todayDate = df.format(new Date());
        return todayDate;
    }

}
