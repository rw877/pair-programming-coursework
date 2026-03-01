package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;

public abstract class Unit {
    public int unitId;
    public int x;
    public int y;
    public int buildingId;
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

    // compulsury abstract methods for 2.2 polymorphism
    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);
}
