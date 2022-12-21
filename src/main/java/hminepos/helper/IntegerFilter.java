package hminepos.helper;

import javafx.scene.control.TextFormatter;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * Created by Mao on 12/21/2022.
 */


public class IntegerFilter implements UnaryOperator<TextFormatter.Change> {
    private final static Pattern DIGIT_PATTERN = Pattern.compile("\\d*");
    @Override
    public TextFormatter.Change apply(TextFormatter.Change change) {
        return DIGIT_PATTERN.matcher(change.getText()).matches() ? change : null;
    }
}
