package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;

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

    // TODO: Helper method for make_move.
    public int calculate_manhattan_distance(int[] incident_coordinates, int[] unit_coordinates) {
        return 0;
    }

    // Makes first legal move that reduces Manhattan distance, searches NESW with vectors.
    public void make_move(Incident incident) {
        for (int i = 0; i < MOVE_VECTORS.length; i++) {
            int[] coordinates = {this.x, this.y};
            calculate_manhattan_distance(incident, coordinates);
        }
    }

    // Compulsory abstract methods for section 2.2 polymorphism:
    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);

    public String getStatus() {
        // TODO: print '-' for no incident, add setter for incident id, implement home.
        return String.format("U#%d TYPE=%S HOME=%d LOC=(%d, %d) STATUS=%S INCIDENT=%d",
                this.unitId, this.type.toString(), 0,  this.x, this.y, this.status.toString(), this.incidentId);
    }
}
