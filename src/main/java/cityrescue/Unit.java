package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import java.lang.Math;

public abstract class Unit {

    final int[][] MOVE_VECTORS = {{1, 0}, {0, 1}, {1, 0}, {0, -1}};
    private int unitId;
    private int x;
    private int y;
    private int buildingId;
    private int incidentId;
    private UnitType type;
    private UnitStatus status;
    private int ticksRemaining;

    public Unit(int unitId, int buildingId, int x, int y, UnitType type) {
        this.unitId = unitId;
        this.buildingId = buildingId;
        this.x = x;
        this.y = y;
        this.type = type;
        this.status = UnitStatus.IDLE;
        // Used as -1 to tell when first arriving to incident.
        this.ticksRemaining = -1;
    }

    // Helper method for make_move returns the manhattan distance between incident and unit.
    public int calculateManhattanDistance(int[] incidentCoordinates, int[] unitCoordinates) {
        return Math.abs(incidentCoordinates[0] - unitCoordinates[0]) +
                Math.abs(incidentCoordinates[1] - unitCoordinates[1]);
    }

    // Makes first legal move that reduces Manhattan distance, searches NESW with vectors.
    public void makeMove(int[] incidentCoordinates, int[] unitCoordinates) {
        int unitDistance = calculateManhattanDistance(incidentCoordinates, unitCoordinates);
        for (int i = 0; i < MOVE_VECTORS.length; i++) {
            int move_x = unitCoordinates[0] + MOVE_VECTORS[i][0];
            int move_y = unitCoordinates[1] + MOVE_VECTORS[i][1];

            // Uses map to check for out of bounds or blocked moves.
            CityMap map = CityRescueImpl.getCityMap();
            if (map.isBlocked(move_x, move_y)) continue;
            if (map.isOutOfBounds(move_x, move_y)) continue;

            int move_distance = calculateManhattanDistance(incidentCoordinates, new int[] {move_x, move_y});
            if (move_distance < unitDistance) {x = move_x; y = move_y;}
        }
    }

    // These abstract classes differ between children and are overridden there.
    public abstract boolean canHandle(IncidentType type);
    public abstract int getTicksToResolve();

    // Getter methods for private variables.
    public int getUnitId() {
        return unitId;
    }

    public int getIncidentId() {return incidentId;}

    public UnitStatus getStatus() {return status;}

    public int getBuildingId() {return buildingId;}

    public UnitType getType() {return type;}

    public int getTicksRemaining() {return ticksRemaining;}

    public int getX() {return x;}

    public int getY() {return y;}

    // Setter methods for private variables.
    public void setStatus(UnitStatus newStatus) {status = newStatus;}

    public void setBuildingId(int newId) {buildingId = newId;}

    public void setX(int newX) {x = newX;}

    public void setY(int newY) {y = newY;}

    public void setIncidentId(int id) {incidentId = id;}

    public void setTicksRemaining(int ticks) {ticksRemaining = ticks;}

    public void decrementTicksRemaining() {ticksRemaining--;}

}
