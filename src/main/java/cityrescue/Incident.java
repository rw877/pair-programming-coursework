package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;


public class Incident {
    private IncidentType type;
    private int incidentId;
    private int x;
    private int y;
    private int severity;
    private int unitId;
    private IncidentStatus status;

    
    public Incident(int incidentId, IncidentType type, int severity, int x, int y) {
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.status = IncidentStatus.REPORTED; 

    }

    public void setStatus(IncidentStatus status_in) {status = status_in;}

    // Getters for Incident.
    public int getIncidentId() {return incidentId;}
    public int getUnitId() {return unitId;}
    public IncidentStatus getStatus() {return status;}
    public IncidentType getType() {return type;}

    public int getX() {return x;}
    public int getY() {return y;}

    public int getSeverity() {return severity;}

    public void setSeverity(int severity_in) {severity = severity_in;}
}
