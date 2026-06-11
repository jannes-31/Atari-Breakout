import sas.*;
import java.awt.Color;

public class bloecke {

    private Rectangle rect;
    private int value;
    private boolean active;

    public bloecke(double x, double y, double width, double height, Color color, int value) {
        this.rect = new Rectangle(x, y, width, height, color);
        this.value = value;
        this.active = true;
    }

    public boolean intersects(Shapes shape) {
        return active && rect.intersects(shape);
    }

    public int getValue() {
        return value;
    }

    public void hit() {
        active = false;
        rect.setHidden(true);
    }

    public boolean isActive() {
        return active;
    }
}
