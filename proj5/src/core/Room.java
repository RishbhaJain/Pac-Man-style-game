package core;

public class Room {

    public int x;
    public int y;
    public int width;
    public int height;

    Room(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int centerRoomX() {
        return x + width / 2;
    }

    public int centerRoomY() {
        return y + height / 2;
    }
}
