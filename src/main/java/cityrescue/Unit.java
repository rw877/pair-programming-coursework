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
    private String incident;
    public UnitType type;
    public UnitStatus status;

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

    public void make_move() {

    }

    // compulsury abstract methods for 2.2 polymorphism
    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);

    public String getStatus() {
        return String.format("U#%d TYPE=%S HOME=%d LOC=(%d, %d) STATUS=%S INCIDENT=%d",
                this.unitId, this.type, this.x, this.y, this.status, this.incident);
    }
}
