package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * This class extends the CityRescue interface and overrides its methods.
 */
public class CityRescueImpl implements CityRescue {
    private static CityMap map;

    // Constant array size limits
    private static final int MAX_STATIONS = 20;
    private static final int MAX_UNITS = 50;
    private static final int MAX_INCIDENTS = 200;

    // Arrays for each class
    private static Station[] stations;
    private static Unit[] units;
    private static Incident[] incidents;

    // Counters start from 1
    private int nextStation = 1;
    private int nextUnit = 1;
    private int nextIncident = 1;

    private int ticks = 0;

    /**
     * Creates a map, given a width and height, creates station, unit and incident arrays with predetermined size.
     *
     * @param width gives the width of the map.
     * @param height gives the height of the map.
     * @throws InvalidGridException if width or height are zero or negative.
     */
    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        // check for valid grid dimensions
        if (width <= 0 || height <= 0) {
            throw new InvalidGridException("Width and height are invalid.");
        }
        // setup grid and arrays
        this.map = new CityMap(width, height);
        this.stations = new Station[MAX_STATIONS];
        this.units = new Unit[MAX_UNITS];
        this.incidents = new Incident[MAX_INCIDENTS];
    }

    /**
     * Returns an int array pair of width and height of the map using getter functions.
    */
    @Override
    public int[] getGridSize() {
        return new int[]{map.getWidth(), map.getHeight()};
    }

    /**
     * Calls the setter object of the map class using the input coordinates and a true boolean value to place the barrier.
     *
     * @param x the x coordinate for placing the obstacle.
     * @param y the y coordinate for placing the obstacle.
     * @throws InvalidLocationException based on boolean output of map out of bounds method, given the coordinates.
     */
    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (map.isOutOfBounds(x, y)) {
            throw new InvalidLocationException("X and/or y is out of bounds.");
        }
        map.setObstacle(x, y, true);
    }

    /**
     * Given the coordinates of the obstacle, uses false boolean condition in setObstacle method to remove the obstacle.
     *
     * @param x x coordinate of obstacle.
     * @param y y coordinate of obstacle.
     * @throws InvalidLocationException if x or y are out of bounds, calls CityMap class method.
     */
    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (map.isOutOfBounds(x, y)) {
            throw new InvalidLocationException("X and/or y is out of bounds.");
        }
        map.setObstacle(x, y, false);
    }

    /**
     * Given the name and coordinates, it adds the station to the next element of the station array and increments the
     * station counter to keep track of the number of elements.
     *
     * @param name the input name of the station
     * @param x the x coordinate of the station
     * @param y the y coordinate of the station
     * @throws InvalidNameException if input name is blank
     * @throws InvalidLocationException if the x or y coordinate is out of bounds
     */
    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        if (map.isOutOfBounds(x, y)) throw new InvalidLocationException("X and Y coordinates are not in the bounds.");
        if (map.isBlocked(x, y)) throw new InvalidLocationException("That coordinate is blocked.");
        if (name.isBlank()) throw new InvalidNameException("Name cannot be empty.");

        int stationId = nextStation++; // id points to next array index linearly
        stations[stationId] = new Station(stationId, name, x, y); // adds station to array
        return stationId;
    }

    /**
     * Given a station ID, it finds it and extracts the station parameter from the array by using a boolean condition to
     * shift all the following elements left one space, with the station removed as a pivot. This overwrites the station
     * and ensures there is not a gap in the middle of the array.
     *
     * @param stationId used to extract station
     * @throws IDNotRecognisedException if ID is out of range or at the end if it was not found.
     * @throws IllegalStateException if the station still has units.
     */
    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        if (stations[stationId].getUnitCount() != 0) throw new IllegalStateException("Station that has units cannot be removed.");
        if (stationId < 1 || stationId > MAX_STATIONS || stations[stationId] == null)
            throw new IDNotRecognisedException("ID is outside of range.");

        boolean id_recognised = false;
        // Loops through array and shifts following elements to fill space of removing ID.
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].getStationId() == stationId) {id_recognised = true;}
            if (id_recognised) {stations[i - 1] = stations[i];}
        }
        if (!id_recognised) {
            throw new IDNotRecognisedException("ID was not found.");
        }
    }

    /**
     * Uses station ID to extract station and calls setter to set capacity attribute to new input capacity.
     *
     * @param stationId used to find station
     * @param maxUnits is the new limit for the number of stations
     * @throws IDNotRecognisedException
     * @throws InvalidCapacityException
     */
    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        if (stationId < 1 || stationId > MAX_STATIONS || stations[stationId] == null)
            throw new IDNotRecognisedException("ID not recognised.");

        Station building = stations[stationId];

        if (maxUnits < 0) throw new InvalidCapacityException("Capacity must be positive.");
        if (maxUnits < building.getUnitCount()) throw new InvalidCapacityException("Cannot be less than current unit count");
        
        building.capacity = maxUnits;
    }

    /**
     * Iterates through station array and uses count variable to keep track of how many IDs there are, avoiding spaces
     * with null arrays. It returns a new array of the size count with all the station IDs extracted from a second loop.
     */
    @Override
    public int[] getStationIds() {
        int count = 0;
        for (int i = 1; i < nextStation; i++) {
            if (stations[i] != null) count++;
        }

        int[] result = new int[count];
        int index = 0;
        for (int i = 1; i < nextStation; i++) {
            if (stations[i] != null) {
                result[index++] = stations[i].getStationId();
            }
        }
        return result;
    }

    /**
     * Uses station ID to extract station and its coordinates, so the new unit starts there. It also creates a new unit
     * depending on the input unit type variable, that it adds to the units array.
     *
     * @param stationId used to extract station to add new unit to
     * @param type used to determine which unit class is created
     * @throws IDNotRecognisedException if ID is out of bounds
     * @throws InvalidUnitException if the unit type is null, avoiding null pointer errors
     * @throws IllegalStateException if the station is full
     */
    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        if (stationId < 1 || stationId > MAX_STATIONS || stations[stationId] == null) throw new IDNotRecognisedException("That is not a valid ID");
        if (type == null) throw new InvalidUnitException("Cannot be null type.");

        Station building = stations[stationId]; //lookup station
        int unit_count = building.getUnitCount();
        if (unit_count >= building.capacity) throw new IllegalStateException("Station is full already.");
        if (nextUnit > MAX_UNITS) throw new IllegalStateException("This station has max units.");

        int unitId = nextUnit++;
        int x = building.getX();
        int y = building.getY(); // creates the new unit at stations coordinates

        // Uses polymorphism to add subclasses of unit, depending on type.
        if (type == UnitType.AMBULANCE){
            units[unitId] = new Ambulance(unitId, stationId, x, y);
        } else if (type == UnitType.POLICE_CAR) {
            units[unitId] = new PoliceCar(unitId, stationId, x, y);
        } else { // must be fire engine based on enums
            units[unitId] = new FireEngine(unitId, stationId, x, y);
        }

        building.setUnitCount(unit_count + 1); //update station building to have new unit
        return unitId;
    }

    /**
     * Finds unit and replaces it with null.
     *
     * @param unitId used to extract unit from units
     * @throws IDNotRecognisedException if the unit is out of bounds
     * @throws IllegalStateException if the unit is en route or at scene
     */
    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        // check unit exists
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new IDNotRecognisedException("Unit ID not recognised");
        
        Unit car = units[unitId];
        // is the unit busy check
        if (car.getStatus() == UnitStatus.EN_ROUTE || car.getStatus() == UnitStatus.AT_SCENE) throw new IllegalStateException("Unit is currently EN_ROUTE or AT_SCENE.");

        // lookup units station
        Station station = stations[car.getBuildingId()];
        if (station.getUnitCount() > 0) {
            station.setUnitCount(station.getUnitCount() - 1); // remove one unit from stations unit count
        }

        units[unitId] = null; // unit replaced with null therefore removed/decommissioned
    }

    /**
     * Given a unit ID, it extracts an idle unit and uses encapsulation to link the unit to the station by creating a
     * new station, adding the unit setting a new count, and changing the field variables in unit to mirror this.
     *
     * @param unitId used to add unit to station
     * @param newStationId used to get station
     * @throws IDNotRecognisedException if the unit or station id is out of bounds or null pointer
     * @throws IllegalStateException if the unit is not idle or the station is full
     */
    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // unit exists check
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new IDNotRecognisedException("Unit ID doesn't exist.");
        Unit car = units[unitId];

        // station exists check
        if (newStationId < 1 || newStationId >= stations.length || stations[newStationId] == null) throw new IDNotRecognisedException("Station ID doesn't exist.");
        Station newStation = stations[newStationId]; //new station

        // IDLE unit check
        if (car.getStatus() != UnitStatus.IDLE) throw new IllegalStateException("Unit must be in IDLE state.");
        // space at destination check
        if (newStation.getUnitCount() >= newStation.capacity) throw new IllegalStateException("New station is at full.");
        Station oldStation = stations[car.getBuildingId()]; //original station
        
        if (oldStation.getUnitCount() > 0) {
            oldStation.setUnitCount(oldStation.getUnitCount() - 1); // remove station
        }

        newStation.setUnitCount(newStation.getUnitCount() + 1); // add unit to new station
        car.setBuildingId(newStationId); //change units building
        // change units location to new stations coordinates
        car.setX(newStation.getX());
        car.setY(newStation.getY());
    }

    /**
     *
     */
    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new IDNotRecognisedException("Unit ID doesnt exist.");
        
        Unit car = units[unitId];

        if (outOfService) { // boolean decides toggle of out of service
            if (car.getStatus() != UnitStatus.IDLE) throw new IllegalStateException("Unit must be IDLE");
    
            car.setStatus(UnitStatus.OUT_OF_SERVICE); // outOfService = true hence unit is out service
        } else {
            car.setStatus(UnitStatus.IDLE); // outOfService = false hence unit is in service
        }
    }

    /**
     *
     */
    @Override
    public int[] getUnitIds() {
        // very similar method to get station ids
        int count = 0;
        for (int i = 1; i < nextUnit; i++) {
            if (units[i] != null) count++;
        }

        int[] result = new int[count];
        int index = 0;
        for (int i = 1; i < nextUnit; i++) {
            if (units[i] != null) {
                result[index++] = units[i].getUnitId();
            }
        }
        return result;
    }

    /**
     *
     */
    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new
                IDNotRecognisedException("Unit ID does not exist.");

        Unit unit = units[unitId];

        return String.format("U#%d TYPE=%S HOME=%d LOC=(%d,%d) STATUS=%S INCIDENT=%d", unitId,
                unit.getType().toString(), unit.getBuildingId(),  unit.getX(), unit.getY(),
                unit.getStatus().toString(), unit.getIncidentId());
    }

    /**
     *
     */
    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        //exception checks
        if (type == null) throw new IllegalArgumentException("Cannot be null");
        if (severity < 1 || severity > 5) throw new InvalidSeverityException("Severity must be between 1-5");
        if (map.isOutOfBounds(x, y)) throw new InvalidLocationException("Out of bounds");
        if (map.isBlocked(x, y)) throw new InvalidLocationException("Location blocked");
        
        int incidentId = nextIncident++;
        incidents[incidentId] = new Incident(incidentId, type, severity, x, y); //incident object added to array
        return incidentId;
    }

    /**
     *
     */
    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        if (incidentId < 1 || incidentId >= incidents.length || incidents[incidentId] == null) throw new IDNotRecognisedException("Incident ID not found.");

        Incident incident = incidents[incidentId];
        // Raises invalid state exception if the incident status is not REPORTED or DISPATCHED.
        if (incident.getStatus() != IncidentStatus.REPORTED && incident.getStatus() != IncidentStatus.DISPATCHED) {
            throw new IllegalStateException("Cannot cancel an incident that is not REPORTED or DISPATCHED.");
        }
        // If it was dispatched, then it cancels the incident and sets the unit to idle.
        if (incident.getStatus() == IncidentStatus.DISPATCHED) {
            incidents[incidentId].setStatus(IncidentStatus.CANCELLED);
            units[incident.getUnitId()].setStatus(UnitStatus.IDLE);
        }
    }

    /**
     *
     */
    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        if (incidentId < 1 || incidentId >= incidents.length || incidents[incidentId] == null) throw new IDNotRecognisedException("Incident ID not found.");
        if (incidents[incidentId].getStatus() == IncidentStatus.RESOLVED || incidents[incidentId].getStatus() == IncidentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot escalate an incident that is RESOLVED or CANCELLED.");
        }
        if (newSeverity < 1 || newSeverity > 5) throw new InvalidSeverityException("New severity is too low or high");
        incidents[incidentId].setSeverity(newSeverity);
    }

    /**
     *
     */
    @Override
    public int[] getIncidentIds() {
        int[] incidentIds = new int[incidents.length];
        for (int i = 0; i < incidents.length; i++) {
            incidentIds[i] = incidents[i].getIncidentId();
        }
        return incidentIds;
    }

    /**
     *
     */
    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        if (incidentId < 1 || incidentId >= units.length || units[incidentId] == null) throw new
                IDNotRecognisedException("Unit ID does not exist.");

        Incident incident = incidents[incidentId];
        return String.format("I#%d TYPE=%S SEV=%d LOC=(%d,%d) STATUS=%S UNIT=%d",
                incidentId, incident.getType(), incident.getSeverity(), incident.getX(),
                incident.getY(), incident.getStatus(), incident.getUnitId());
    }

    /**
     *
     */
    public Unit calculateTieBreakers(Unit[] eligibleUnits, int eligibleCount, Incident incident) {
        int minManhattanDistance = 2 * map.getHeight();
        int minDistanceId = -1;
        // Loops through eligible units, using distance, then tiebreakers.
        for (int i = 0; i < eligibleCount; i++) {
            Unit currentUnit = eligibleUnits[i];
            int currentUnitId = currentUnit.getUnitId();
            int current_manhattan_distance = currentUnit.calculateManhattanDistance(new int[]{incident.getX(),
                    incident.getY()}, new int[]{currentUnit.getX(), currentUnit.getY()});

            // Uses manhattan distance to choose the closest eligible unit.
            if (current_manhattan_distance < minManhattanDistance) {
                minManhattanDistance = current_manhattan_distance;
                minDistanceId = currentUnitId;

            } else if (current_manhattan_distance == minManhattanDistance) {
                // Uses other tiebreakers when there is a same minimum Manhattan distance.
                if (currentUnitId == minDistanceId) {
                    if (units[currentUnitId].getBuildingId() < units[minDistanceId].getBuildingId()) {
                        minDistanceId = currentUnitId;
                    }
                } else if (currentUnitId < minDistanceId) {
                    minDistanceId = currentUnitId;
                }
            }
        }
        return units[minDistanceId];
    }

    /**
     *
     */
    public Unit findOptimalUnit(Incident incident) {
        // Saves array of all eligible units, count used to avoid null pointers.
        int eligibleCount = 0;
        Unit[] eligibleUnits = new Unit[units.length + 1];

        // Loop generates list of all eligible units for the given incident.
        for (int i = 1; i < nextUnit; i++) {
            // First, checks that the unit is idle.
            if (units[i].getStatus() == UnitStatus.IDLE) {
                // Then, checks to see that the incident type corresponds to the unit type.
                if (units[i].getType().ordinal() == incident.getType().ordinal()) {
                    eligibleUnits[eligibleCount] = units[i];
                    eligibleCount++;
                }
            }
        }
        // Helper method uses manhattan distances and IDs to determine the optimal unit.
        return calculateTieBreakers(eligibleUnits, eligibleCount, incident);
    }

    /**
     *
     */
    @Override
    public void dispatch() {
        for (int i = 1; i < nextIncident; i++) {
            if (incidents[i].getStatus() == IncidentStatus.REPORTED) {
                Unit optimal_unit = findOptimalUnit(incidents[i]);
                // Calls helper method to find the optimal eligible unit based on ID order, distance and compatibility.
                optimal_unit.setStatus(UnitStatus.EN_ROUTE);
                optimal_unit.setIncidentId(i);
                incidents[i].setUnitId(optimal_unit.getUnitId());
                incidents[i].setStatus(IncidentStatus.DISPATCHED);
            }
        }
    }

    /**
     *
     */
    public void unitTick() {
        // Iterates through all units in ascending ID order, updates movements and status.
        for (int i = 1; i < nextUnit; i++) {
            if (units[i].getStatus() == UnitStatus.EN_ROUTE) {
                Incident incident = incidents[units[i].getIncidentId()];
                units[i].makeMove(new int[] {incident.getX(), incident.getY()}, new int[] {units[i].getX(), units[i].getY()});

                if (units[i].calculateManhattanDistance(new int[] {incident.getX(), incident.getY()},
                        new int[] {units[i].getX(), units[i].getY()}) == 0) {
                    units[i].setStatus(UnitStatus.AT_SCENE);
                }
            }
        }
    }

    /**
     *
     */
    public void incidentTick() {
        // Iterates through all incidents in ascending ID order, decreases counter or resolves.
        for (int i = 1; i < nextIncident; i++) {
            Unit incidentUnit = units[incidents[i].getUnitId()];
            if (incidentUnit.getStatus() == UnitStatus.AT_SCENE) {
                // If the first time arriving, it sets remaining ticks as max for subclass.
                if (incidentUnit.getTicksRemaining() == -1) {
                    incidentUnit.setTicksRemaining(incidentUnit.getTicksToResolve());
                } else {
                    // Otherwise. it decrements the tick count.
                    incidentUnit.decrementTicksRemaining();
                }

                // Checks if ticks are zero to resolve incident.
                if (incidentUnit.getTicksRemaining() == 0) {
                    incidentUnit.setStatus(UnitStatus.IDLE);
                    incidents[i].setStatus(IncidentStatus.RESOLVED);
                }
            }
        }
    }

    /**
     *
     */
    @Override
    public void tick() {
        ticks ++;
        dispatch();
        unitTick();
        incidentTick();
    }

    /**
     *
     */
    @Override
    public String getStatus() {
        String statusReport = "";
        statusReport += String.format("TICK=%d\nSTATIONS=%d UNITS=%d INCIDENTS=%d OBSTACLES=%d\n", ticks,
                stations.length, units.length, incidents.length, map.countObstacles());
        statusReport += "INCIDENTS\n";
        // Loop adds from class status method for all the incidents.
        for (int i = 1; i < nextIncident; i++) {
            // Try catch block handles exception from viewIncident method without compromising the overridden class.
            try {
                statusReport += viewIncident(incidents[i].getIncidentId());
            } catch (IDNotRecognisedException e) {
                e.printStackTrace();
            }
        }
        statusReport += "\nUNITS\n";
        // Loop adds from class status method for all the units.
        for (int i = 1; i < nextUnit; i++) {
            // Try catch block handles exception from viewUnit method without compromising the overridden class.
            try {
                statusReport += viewUnit(units[i].getUnitId());
            } catch (IDNotRecognisedException e) {
                e.printStackTrace();
            }
        }
        return statusReport;
    }

    public static CityMap getCityMap() {return map;}
}
