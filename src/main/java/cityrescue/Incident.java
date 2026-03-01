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
    public IncidentStatus status; // examples of encapsulation

    
    public Incident(int incidentId, IncidentType type, int severity, int x, int y) {
        this.incidentId = incidentId;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        this.status = IncidentStatus.REPORTED; 

    }

    public String getStatus() {
        return String.format("I#%d TYPE=%S SEV=%d LOC=(%d, %d) STATUS=%S UNIT=%d",
                this.incidentId, this.type, this.severity, this.x, this.y, this.status, this.unit);
    }
}
