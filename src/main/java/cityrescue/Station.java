package cityrescue;

public class Station {
    private String name;
    private int x_coordinate;
    private int y_coordinate;
    public int capacity;
    public int units;
    private int stationId;

    public Station(int stationId, String name_in, int x_in, int y_in) {
        this.stationId = stationId;
        this.name = name_in;
        this.x_coordinate = x_in;
        this.y_coordinate = y_in;
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
}
