package com.mycompany.emergency_medical_dispatch;

public class Call {
    public int severity;        // 1 = critical, 2 = medium, 3 = low
    public String description;  // e.g. "Heart attack"
    public String location;     // e.g. "Location A"

    // Constructor
    public Call(int severity, String description, String location) {
        this.severity = severity;
        this.description = description;
        this.location = location;
    }

    // So we can print a Call nicely
    @Override
    public String toString() {
        return description + " at " + location + " (Severity " + severity + ")";
    }
}
