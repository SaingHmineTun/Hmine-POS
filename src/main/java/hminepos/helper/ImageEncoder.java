package hminepos.helper;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class ImageEncoder {
    public static String encodeToString(Image image, String type) {
        String imageString = null;
        BufferedImage bImage = fxToBufferedImage(image);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(bImage, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.getEncoder().encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static Image decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            imageByte = Base64.getDecoder().decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bufferedImagetoFX(image);
    }

    private static BufferedImage fxToBufferedImage(Image image) {

        return SwingFXUtils.fromFXImage(image, null);
    }

    private static Image bufferedImagetoFX(BufferedImage bImage) {

        return SwingFXUtils.toFXImage(bImage, null);
    }

}
