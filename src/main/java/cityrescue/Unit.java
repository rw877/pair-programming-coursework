package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import java.lang.Math;

public abstract class Unit {

    // Used to move units
    final int[][] MOVE_VECTORS = {{1, 0}, {0, 1}, {1, 0}, {0, -1}};
    public int unitId;
    public int x;
    public int y;
    public int buildingId;
    private int incidentId;
    public UnitType type;
    public UnitStatus status;

    private int ticks_remaining_at_scene;

    public Unit(int unitId, int buildingId, int x, int y, UnitType type) {
        this.unitId = unitId;
        this.buildingId = buildingId;
        this.x = x;
        this.y = y;
        this.type = type;
        this.status = UnitStatus.IDLE;
    }

    public int get_unit_id() {
        return unitId;
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
            // TODO: Check for legal move OOB or obstacle.
            int move_distance = calculate_manhattan_distance(incident_coordinates, new int[] {move_x, move_y});
            if (move_distance < unit_distance) {x = move_x; y = move_y;}
        }
    }

    // Called by tick(), updates each unit based on status.
    public void unit_tick() {
        switch (status) {
            case UnitStatus.IDLE:
                // TODO: Implement and call dispatch.
                CityRescueImpl.dispatch();
                status = UnitStatus.EN_ROUTE;
                break;
            case UnitStatus.EN_ROUTE:
                make_move(new int[] {}, new int[] {x, y});
                // TODO: Use incident ID to get coords for manhattan distance.
                if (calculate_manhattan_distance(new int[] {}, new int[] {x, y}) == 0) {status = UnitStatus.AT_SCENE;}
                break;
            case UnitStatus.AT_SCENE:
                // TODO: Implement Resolve Incident.
                ticks_remaining_at_scene--;
                if (ticks_remaining_at_scene == 0) {status = UnitStatus.IDLE;}
        }
    }

    // Compulsory abstract methods for section 2.2 polymorphism:
    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);

    public String getStatus() {
        // TODO: Change this to viewStatus in Impl
        // TODO: print '-' for no incident, add setter for incident id, implement home.
        return String.format("U#%d TYPE=%S HOME=%d LOC=(%d, %d) STATUS=%S INCIDENT=%d",
                this.unitId, this.type.toString(), 0,  this.x, this.y, this.status.toString(), this.incidentId);
    }
}
