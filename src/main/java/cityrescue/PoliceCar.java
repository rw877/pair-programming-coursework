package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

public class PoliceCar extends Unit {
    public PoliceCar(int unitId, int buildingId, int x, int y) {
        // pass unit type to parent class unit
        super(unitId, buildingId, x, y, UnitType.POLICE_CAR);
    }

    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.CRIME;
    }

    @Override
    public int getTicksToResolve() {
        return 3;
    }
}
