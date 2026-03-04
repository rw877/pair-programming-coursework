package cityrescue;

public class Station {
    private String name;
    private int x;
    private int y;
    public int capacity;
    private int unitCount;
    private int stationId;

    final int CAPACITY = 10;

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
