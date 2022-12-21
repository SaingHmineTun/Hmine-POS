package hminepos.helper;

/**
 * Created by SaiMao on 5/15/2017.
 */
public class ItemEvent {
    private int value;
    private int index;

    public ItemEvent(int index, int value) {
        this.value = value;
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
