package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;

public class Ambulance extends Unit {
    public Ambulance(int unitId, int buildingId, int x, int y) {
        // pass unit type to parent class unit
        super(unitId, buildingId, x, y, UnitType.AMBULANCE);
    }

    // all decisions forced by coursework rules
    @Override
    public boolean canHandle(IncidentType type) {
        return type == IncidentType.MEDICAL;
    }

    @Override
    public int getTicksToResolve(int severity) {
        return 2;
    }
}
