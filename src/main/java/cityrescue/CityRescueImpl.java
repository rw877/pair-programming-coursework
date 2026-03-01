package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {

    // Added fields
    // map
    private CityMap map;
    private int ticks = 0;

    // storage limits
    private static final int max_stations = 20;
    private static final int max_units = 50;
    private static final int max_incidents = 200;

    // arrays
    private Station[] stations;
    private Unit[] units;
    private Incident[] incidents;

    // counters
    private int nextStation = 1;
    private int nextUnit = 1;
    private int nextIncident = 1;

    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        // check for valid grid dimensions
        if (width <= 0 || height <= 0) {
            throw new InvalidGridException("Width and height are invalid.");
        }
        // setup grid and arrays
        this.map = new CityMap(width, height);
        this.stations = new Station[max_stations];
        this.units = new Unit[max_units];
        this.incidents = new Incident[max_incidents];
        // setup counters
        this.nextStation = 1;
        this.nextUnit = 1;
        this.nextIncident = 1;
        this.ticks = 0;

    }

    @Override
    public int[] getGridSize() {
        return new int[]{map.getWidth(), map.getHeight()};
    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (map.isOutOfBounds(x, y)) {
            throw new InvalidLocationException("X and/or y is out of bounds.");
        }
        map.setObstacle(x, y, true);
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (map.isOutOfBounds(x, y)) {
            throw new InvalidLocationException("X and/or y is out of bounds.");
        }
        map.setObstacle(x, y, false);
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        if (map.isOutOfBounds(x, y)) throw new InvalidLocationException("X and Y coordinates are not in the bounds.");
        if (map.isBlocked(x, y)) throw new InvalidLocationException("That coordinate is blocked.");
        if (name.isBlank()) throw new InvalidNameException("Name cannot be empty.");

        int stationId = nextStation++; // id points to next array index linearly
        stations[stationId] = new Station(stationId, name, x, y); // adds station to array
        return stationId;
    }

    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: add IllegalStateException
        boolean id_recognised = false;
        // Loops through array and shifts following elements to fill space of removing ID.
        for (int i = 0; i < stations.length; i++) {
            if (stations[i].get_station_id() == stationId) {id_recognised = true;}
            if (id_recognised) {stations[i - 1] = stations[i];}
        }
        if (!id_recognised) {throw new IDNotRecognisedException("Station ID not found.");}

    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        Station building = stations[stationId];

        if (maxUnits < 0) throw new InvalidCapacityException("Capacity must be positive.");
        if (maxUnits < building.units) throw new InvalidCapacityException("Cannot be less than current unit count");
        
        building.capacity = maxUnits;
    }

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
                result[index++] = stations[i].get_station_id();
            }
        }
        return result;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        if (stationId < 1 || stationId >= 21 || stations[stationId] == null) throw new IDNotRecognisedException("That is not a valid ID");
        if (type == null) throw new InvalidUnitException("Cannot be null type.");

        Station building = stations[stationId]; //lookup station
        if (building.units >= building.capacity) throw new IllegalStateException("Station is full already.");
        if (nextUnit > max_units) throw new IllegalStateException("This station has max units.");

        int unitId = nextUnit++;
        int x = building.get_x_coordinate();
        int y = building.get_y_coordinate(); // creates the new unit at stations coordinates

        //nice example of polymorphism
        if (type == UnitType.AMBULANCE){
            units[unitId] = new Ambulance(unitId, stationId, x, y);
        } else if (type == UnitType.POLICE_CAR) {
            units[unitId] = new PoliceCar(unitId, stationId, x, y);
        } else { // must be fire engine based on enums
            units[unitId] = new FireEngine(unitId, stationId, x, y);
        }

        building.units++; //update station building to have new unit
        return unitId;
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        // check unit exists
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new IDNotRecognisedException("Unit ID not recognised");
        
        Unit car = units[unitId];
        // is the unit busy check
        if (car.status == UnitStatus.EN_ROUTE || car.status == UnitStatus.AT_SCENE) throw new IllegalStateException("Unit is currently EN_ROUTE or AT_SCENE.");

        // lookup units station
        Station station = stations[car.buildingId];
        if (station.units > 0) {
            station.units--; // remove one unit from stations unit count
        }

        units[unitId] = null; // unit replaced with null therefore removed/decomissioned
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // unit exists check
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new IDNotRecognisedException("Unit ID doesnt exist.");
        Unit car = units[unitId];

        // station exists check
        if (newStationId < 1 || newStationId >= stations.length || stations[newStationId] == null) throw new IDNotRecognisedException("Station ID doesnt exit.");
        Station newStation = stations[newStationId]; //new station

        // IDLE unit check
        if (car.status != UnitStatus.IDLE) throw new IllegalStateException("Unit must be in IDLE state.");
        // space at destination check
        if (newStation.units >= newStation.capacity) throw new IllegalStateException("New station is at full.");
        Station oldStation = stations[car.buildingId]; //original station
        
        if (oldStation.units > 0) {
            oldStation.units--; // remove station
        }

        newStation.units++; // add unit to new station
        car.buildingId = newStationId; //change units building
        // change units location to new stations coordinates
        car.x = newStation.get_x_coordinate();
        car.y = newStation.get_y_coordinate(); 
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new IDNotRecognisedException("Unit ID doesnt exist.");
        
        Unit car = units[unitId];

        if (outOfService) { // boolean decides toggle of out of service
            if (car.status != UnitStatus.IDLE) throw new IllegalStateException("Unit must be IDLE"); 
    
            car.status = UnitStatus.OUT_OF_SERVICE; // outOfService = true hence unit is out service
        } else {
            car.status = UnitStatus.IDLE; // outOfService = false hence unit is in service
        }
    }

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
                result[index++] = units[i].get_unit_id(); 
            }
        }
        return result;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        if (unitId < 1 || unitId >= units.length || units[unitId] == null) throw new IDNotRecognisedException("Unit ID doesnt exist.");
        
        Unit car = units[unitId];

        return "U#" + car.unitId + 
               " TYPE=" + car.type + 
               " HOME=" + car.buildingId + 
               " LOC=(" + car.x + "," + car.y + ")" + 
               " STATUS=" + car.status; // TODO: add incident and work values
    }

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

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        if (incidentId < 1 || incidentId >= incidents.length || incidents[incidentId] == null) throw new IDNotRecognisedException("Incident ID not found.");
        
        Incident incident = incidents[incidentId];
        // check if incident is REPORTED or DISPATCHED only
        if (incident.status != IncidentStatus.REPORTED && incident.status != IncidentStatus.DISPATCHED) {
            throw new IllegalStateException("Cannot cancel an incident that is not REPORTED or DISPATCHED.");
        }

        //TODO: finish method
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getIncidentIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void dispatch() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    // Tick updates movements with manhattan rule, arrivals, on-scene, resolution.
    public void tick() {
        // TODO: Add case for arrivals, check on scene to update, resolve issues
        // TODO: Call helper methods
        for (int i = 0; i < units.length; i++) {
            units[i].unit_tick();
        }
        ticks ++;
    }

    @Override
    // Returns specifically formatted string as report of ticks, incidents and units.
    public String getStatus() {
        String status_report = "";
        // TODO: Method to count obstacles in map class
        status_report += String.format("TICK=%d\nSTATIONS=%d UNITS=%d INCIDENTS=%d OBSTACLES=%d\n", ticks,
                stations.length, units.length, incidents.length, 0);
        status_report += "INCIDENTS\n";
        // Loop adds from class status method for all the incidents.
        for (int i = 1; i < nextIncident; i++) {
            status_report += incidents[i].getStatus();
        }
        status_report += "UNITS\n";
        // Loop adds from class status method for all the units.
        for (int i = 1; i < nextUnit; i++) {
            status_report += units[i].getStatus();
        }
        return status_report;
    }
}
