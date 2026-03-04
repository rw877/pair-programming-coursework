package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

public class FireEngine extends Unit {
    public FireEngine(int unitId, int buildingId, int x, int y) {
        // pass unit type to parent class unit
        super(unitId, buildingId, x, y, UnitType.FIRE_ENGINE);
    }

    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.FIRE;
    }

    @Override
    public int getTicksToResolve() {
        return 4;
    }
}
