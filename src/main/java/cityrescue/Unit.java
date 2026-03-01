package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;

public abstract class Unit {
    public int unitId;
    public int x;
    public int y;
    public int buildingId;
    public UnitStatus status;
    public UnitType type;

    public Unit(int unitId, int buildingId, int x, int y, UnitStatus status, UnitType type) {
        this.unitId = unitId;
        this.buildingId = buildingId;
        this.x = x;
        this.y = y;
        this.status = status;
        this.type = type;
    }

    // compulsury abstract methods for 2.2 polymorphism
    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve(int severity);
}
