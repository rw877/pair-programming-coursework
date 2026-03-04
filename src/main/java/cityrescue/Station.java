package cityrescue;

/**
 * Station class is also mainly getters and setters, stores name, coordinates, capacity, number of units and ID.
 */
public class Station {
    private String name;
    private int x;
    private int y;
    public int capacity;
    private int unitCount;
    private int stationId;

    final int CAPACITY = 10;

    /**
     * Constructor for station class sets capacity as predefined constant value, the rest as input variables.
     *
     * @param stationId used in CityResculeImpl
     * @param name used as label for station
     * @param x x coordinate
     * @param y y coordinate
     */
    public Station(int stationId, String name, int x, int y) {
        this.stationId = stationId;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = CAPACITY;
    }

    public int getStationId() {return stationId;}

    public int getX() {return x;}

    public int getY() {return y;}

    public int getUnitCount() {return unitCount;}

    public void setUnitCount(int newCount) {unitCount = newCount;}
}
