package hminepos.helper;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by SaiMao on 4/9/2017.
 */
public class CurrentTime extends Label{

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    public CurrentTime(){
        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0), event -> {
            setText(LocalTime.now().format(formatter));
        });
        KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1));
        Timeline timeline = new Timeline(keyFrame1, keyFrame2);
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}
