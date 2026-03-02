package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;


public class Incident {
    private IncidentType type;
    private int incidentId;
    private int x;
    private int y;
    private int severity;
    private int unit;
    private IncidentStatus status;

    
    public Incident(int incidentId, IncidentType type, int severity, int x, int y) {
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.status = IncidentStatus.REPORTED; 

    }

    // Getters for Incident.
    public int getIncidentId() {return incidentId;}
    public IncidentStatus getStatus() {return status;}
    public IncidentType getType() {return type;}
}
