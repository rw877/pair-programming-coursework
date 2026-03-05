package cityrescue;

/**
 * Class has width, height and obstacles on the map, only keeps obstacles 2d array because they are not a class with
 * coordinates stored. Also includes method to count obstacles, in addition to setters and getters.
 */
public class CityMap {
    private int width;
    private int height;
    private boolean[][] obstacles;

    /**
     * Constructor for CityMap class takes width and height only as dimensions.
     */
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

    public int countObstacles() {
        int obstacleCount = 0;
        for (int i = 0; i < obstacles.length; i++) {
            for (int j = 0; j < obstacles[i].length; j++) {
                if (isBlocked(i, j)) obstacleCount++;
            }
        }
        return obstacleCount;
    }

}
