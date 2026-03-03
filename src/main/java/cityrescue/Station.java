package cityrescue;

public class Station {
    private String name;
    private int x_coordinate;
    private int y_coordinate;
    public int capacity;
    private int unit_count;
    private int stationId;

    final int CAPACITY = 10;

    public Station(int stationId, String name_in, int x_in, int y_in) {
        this.stationId = stationId;
        this.name = name_in;
        this.x_coordinate = x_in;
        this.y_coordinate = y_in;
        // TODO: Add capacity to constructor or setter?
        this.capacity = CAPACITY;
    }

    public int get_station_id() {
        return stationId;
    }

    public int get_x_coordinate() {
        return x_coordinate;
    }

    public int get_y_coordinate() {
        return y_coordinate;
    }

    public int getUnit_count() {return unit_count;}
    public void setUnit_count(int new_count) {unit_count = new_count;}
}
