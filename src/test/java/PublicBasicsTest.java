import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class PublicBasicsTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }

    @Test
    void initialise_setsGridSize_andResetsTick() {
        int[] sz = cr.getGridSize();
        assertArrayEquals(new int[]{5,5}, sz);
        assertTrue(cr.getStatus().contains("TICK=0"));
    }

    @Test
    void addStation_assignsIdStartingAt1() throws Exception {
        int id1 = cr.addStation("Central", 1, 1);
        int id2 = cr.addStation("North", 1, 2);
        assertEquals(1, id1);
        assertEquals(2, id2);
    }

    @Test
    void addObstacle_outOfBounds_throws_andStateUnchanged() throws Exception {
        String before = cr.getStatus();
        assertThrows(InvalidLocationException.class, () -> cr.addObstacle(-1, 0));
        assertEquals(before, cr.getStatus());
    }

    @Test
    void addObstacle_andRemoveObstacle() throws Exception {
        cr.addObstacle(0, 0);
        assertEquals(1, CityRescueImpl.getCityMap().countObstacles());
        cr.removeObstacle(0, 0);
        assertEquals(0, CityRescueImpl.getCityMap().countObstacles());
    }

    @Test
    void addUnit() throws Exception {
        int stationId1 = cr.addStation("Police", 0, 0);

        int unitId1 = cr.addUnit(stationId1, UnitType.POLICE_CAR);
        int unitId2 = cr.addUnit(stationId1, UnitType.POLICE_CAR);
        assertEquals(2, cr.getUnitIds().length);

        // Tests exceptions for adding a unit.
        String beforeInvalidId = cr.getStatus();
        assertThrows(IDNotRecognisedException.class, () -> cr.addUnit(-1, UnitType.POLICE_CAR));
        assertEquals(beforeInvalidId, cr.getStatus());

        //  Sets station capacity to zero for testing full unit exception.
        int stationId2 = cr.addStation("Full", 2, 2);
        CityRescueImpl.getStations()[stationId2].setCapacity(0);
        assertEquals(0, CityRescueImpl.getStations()[stationId2].getCapacity());

        String beforeFullUnit = cr.getStatus();
        assertThrows(IllegalStateException.class, () -> cr.addUnit(stationId2, UnitType.POLICE_CAR));
        assertEquals(beforeFullUnit, cr.getStatus());
    }

    @Test
    void decommissionUnit() throws Exception {
        int stationId1 = cr.addStation("Police", 0, 0);

        int unitId1 = cr.addUnit(stationId1, UnitType.POLICE_CAR);
        int unitId2 = cr.addUnit(stationId1, UnitType.POLICE_CAR);
        assertEquals(2, cr.getUnitIds().length);

        // Tests decommission unit.
        cr.decommissionUnit(unitId2);
        assertEquals(1, cr.getUnitIds().length);

        String beforeFakeId = cr.getStatus();
        assertThrows(IDNotRecognisedException.class, () -> cr.decommissionUnit(-1));
        assertEquals(beforeFakeId, cr.getStatus());

        CityRescueImpl.getUnits()[unitId1].setStatus(UnitStatus.EN_ROUTE);

        String beforeRemoveActiveUnit = cr.getStatus();
        assertThrows(IllegalStateException.class, () -> cr.decommissionUnit(unitId1));
        assertEquals(beforeRemoveActiveUnit, cr.getStatus());
    }

    @Test
    void transferUnit() throws Exception {
        int stationId1 = cr.addStation("Police", 0, 0);
        int stationId2 = cr.addStation("SWAT Team", 0, 0);

        int unitId1 = cr.addUnit(stationId1, UnitType.POLICE_CAR);
        int unitId2 = cr.addUnit(stationId1, UnitType.POLICE_CAR);

        cr.transferUnit(unitId2, stationId2);
        assertEquals(1, CityRescueImpl.getStations()[stationId2].getUnitCount());

        // Tests invalid ID exceptions:
        String beforeInvalidUnitId = cr.getStatus();
        assertThrows(IDNotRecognisedException.class, () -> cr.transferUnit(-1, stationId2));
        assertEquals(beforeInvalidUnitId, cr.getStatus());

        String beforeInvalidStationId = cr.getStatus();
        assertThrows(IDNotRecognisedException.class, () -> cr.transferUnit(unitId1, -1));
        assertEquals(beforeInvalidStationId, cr.getStatus());


        // Tests IllegalStateException
        CityRescueImpl.getUnits()[unitId1].setStatus(UnitStatus.EN_ROUTE);

        String beforeActiveUnit = cr.getStatus();
        assertThrows(IllegalStateException.class, () -> cr.transferUnit(unitId1, stationId2));
        assertEquals(beforeActiveUnit, cr.getStatus());
    }

    @Test
    void addStation() throws Exception {
        int stationId1 = cr.addStation("Police", 0, 0);
        int stationId2 = cr.addStation("The Police Police", 1, 1);
        assertEquals(2, cr.getStationIds().length);

        String beforeAddBlank = cr.getStatus();
        assertThrows(InvalidNameException.class, () -> cr.addStation("", 0, 1));
        assertEquals(beforeAddBlank, cr.getStatus());

        String beforeAddLocation = cr.getStatus();
        assertThrows(InvalidLocationException.class, () -> cr.addStation("Police Academy", -1, 0));
        assertEquals(beforeAddLocation, cr.getStatus());

        cr.addObstacle(2, 2);
        String beforeAddBlocked = cr.getStatus();
        assertThrows(InvalidLocationException.class, () -> cr.addStation("Police Academy", 2, 2));
        assertEquals(beforeAddBlocked, cr.getStatus());
    }
    @Test
    void remove_Station() throws Exception{
        int stationId1 = cr.addStation("Police", 0, 0);
        int stationId2 = cr.addStation("The Police Police", 1, 1);

        // Tests remove station:
        int unitId1 = cr.addUnit(stationId1, UnitType.POLICE_CAR);
        cr.removeStation(stationId2);
        assertEquals(1, cr.getStationIds().length);

        String beforeRemoveStation = cr.getStatus();
        assertThrows(IllegalStateException.class, () -> cr.removeStation(stationId1));
        assertEquals(beforeRemoveStation, cr.getStatus());

        String beforeRemoveStationID = cr.getStatus();
        assertThrows(IDNotRecognisedException.class, () -> cr.removeStation(-1));
        assertEquals(beforeRemoveStationID, cr.getStatus());
    }

    @Test
    void removeStation() throws Exception {
        String before = cr.getStatus();
        assertThrows(InvalidLocationException.class, () -> cr.addObstacle(-1, 0));
        assertEquals(before, cr.getStatus());
    }
}
