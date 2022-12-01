package hminepos.helper;

import com.password4j.Password;

/**
 * Created by Mao on 12/1/2022.
 */


public class Utils {

    public static boolean checkPassword(String hashPassword, String yourPassword) {

        return Password.check(yourPassword, hashPassword).withArgon2();
    }

}
