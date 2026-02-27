package cityrescue;

import javax.naming.InvalidNameException;

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
    private int nextStation;
    private int nextUnit;
    private int nextIncident;

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
        this.nextStation = 0;
        this.nextUnit = 0;
        this.nextIncident = 0;
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

        int id = nextStation++; // id points to next array index linearly
        stations[id] = new Station(id, name, x, y); // adds station to array
        return id;
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
        int[] result = new int[20]; // because max stations is 20
        for (int i = 0; i <= stations.length; i++) {
            result[i] = stations[i].get_station_id();
        }
        return result;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getUnitIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
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
    public void tick() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getStatus() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
