package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import java.lang.Math;

public abstract class Unit {

    // Used to move units
    final int[][] MOVE_VECTORS = {{1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private int unitId;
    private int x;
    private int y;
    private int buildingId;
    private int incidentId;
    private UnitType type;
    private UnitStatus status;
    private int ticks_remaining_at_scene;

    public Unit(int unitId, int buildingId, int x, int y, UnitType type) {
        this.unitId = unitId;
        this.buildingId = buildingId;
        this.x = x;
        this.y = y;
        this.type = type;
        this.status = UnitStatus.IDLE;
    }

    // Helper method for make_move calculates the manhattan distance between incident and unit.
    public int calculate_manhattan_distance(int[] incident_coordinates, int[] unit_coordinates) {
        return Math.abs(incident_coordinates[0] - unit_coordinates[0]) +
                Math.abs(incident_coordinates[1] - unit_coordinates[1]);
    }

    // Makes first legal move that reduces Manhattan distance, searches NESW with vectors.
    public void make_move(int[] incident_coordinates, int[] unit_coordinates) {
        int unit_distance = calculate_manhattan_distance(incident_coordinates, unit_coordinates);
        for (int i = 0; i < MOVE_VECTORS.length; i++) {
            int move_x = unit_coordinates[0] + MOVE_VECTORS[i][0];
            int move_y = unit_coordinates[1] + MOVE_VECTORS[i][1];
            // TODO: Use map to check for legal move OOB or obstacle.

            int move_distance = calculate_manhattan_distance(incident_coordinates, new int[] {move_x, move_y});
            if (move_distance < unit_distance) {x = move_x; y = move_y;}
        }
    }

    // Called by tick(), updates each unit based on status.
    public void unit_tick() {
        switch (status) {
            case IDLE:
                status = UnitStatus.EN_ROUTE;
                break;
            case EN_ROUTE:
                make_move(new int[] {}, new int[] {x, y});
                // Uses getter and incidents to get x and y from incident ID.
                Incident[] incident_array = CityRescueImpl.getIncidents();
                Incident incident = incident_array[incidentId];
                if (calculate_manhattan_distance(new int[] {incident.getX(), incident.getY()}, new int[] {x, y}) == 0) {
                    status = UnitStatus.AT_SCENE;
                }
                break;
            case AT_SCENE:
                // TODO: Implement Resolve Incident.
                ticks_remaining_at_scene--;
                if (ticks_remaining_at_scene == 0) {status = UnitStatus.IDLE;}
        }
    }

    // Compulsory abstract methods for section 2.2 polymorphism:
    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);


    // Getter methods for private variables.
    public int get_unit_id() {
        return unitId;
    }
    public int get_incident_id() {return incidentId;}
    public UnitStatus getStatus() {return status;}
    public int getBuildingId() {return buildingId;}
    public UnitType getType() {return type;}
    public int getX() {return x;}
    public int getY() {return y;}


    // Setter methods for private variables.
    public void setStatus(UnitStatus status_in) {status = status_in;}
    public void setBuildingId(int new_id) {buildingId = new_id;}
    public void setX(int x_in) {x = x_in;}
    public void setY(int y_in) {y = y_in;}

}
