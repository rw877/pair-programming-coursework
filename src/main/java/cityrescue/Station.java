package cityrescue;

public class Station {
    private String name;
    private int x_coordinate;
    private int y_coordinate;

    private int station_id;
    public Station(String name_in, int x_in, int y_in) {
        this.name = name_in;
        this.x_coordinate = x_in;
        this.y_coordinate = y_in;
    }

    public int get_station_id() {
        return station_id;
    }
}
