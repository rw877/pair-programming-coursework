package cityrescue;

public class CityMap {
    private int width;
    private int height;
    private boolean[][] obstacles;
    
    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.obstacles = new boolean[width][height];
    }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isOutOfBounds(int x, int y) {
        return x < 0 || x >= width || y < 0 || y >= height;
    }

    public boolean isBlocked(int x, int y) {
        if (isOutOfBounds(x, y)) return true;
        return obstacles[x][y];
    }

    public void setObstacle(int x, int y, boolean blocked) {
        if (!isOutOfBounds(x, y)) {
            obstacles[x][y] = blocked;
        }
    }
}
