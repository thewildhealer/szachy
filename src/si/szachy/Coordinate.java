package si.szachy;

import java.io.Serializable;

public class Coordinate implements Serializable {
    public final int x, y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public boolean isValid() {
        return isValid(this);
    }
    public boolean isValid(Coordinate c) {
        return isValid(c.x, c.y);
    }
    public boolean isValid(int x, int y) {
        return (x >= 0 && y >= 0 && x <= 7 && y <= 7);
    }
}
