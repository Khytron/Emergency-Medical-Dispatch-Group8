/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.emergency_medical_dispatch;

import java.util.*;

//dont add this 
//TESTING ONLY
public class EmergencyDispatch {
    public static void main(String[] args) {
        // 1. Create the graph instance
        Graph graph = new Graph();

        // 2. Add the city map locations (Nodes)
        graph.addLocation("Location A");
        graph.addLocation("Location B");
        graph.addLocation("Location C");
        graph.addLocation("Hospital Kuala Lumpur");
        graph.addLocation("Hospital Selangor");

        // 3. Connect them with roads and travel times (Edges & Weights)
        // setting up two different paths to reach Location A
        graph.addRoad("Hospital Kuala Lumpur", "Location C", 7);
        graph.addRoad("Location C", "Location A", 3);   // Total ETA from Alpha to A = 10 mins
        
        graph.addRoad("Hospital Selangor", "Location B", 4);
        graph.addRoad("Location B", "Location A", 10);  // Total ETA from Beta to A = 14 mins

        // 4. Set active ambulance locations
        List<String> availableAmbulances = new ArrayList<>();
        availableAmbulances.add("Hospital Kuala Lumpur");
        availableAmbulances.add("Hospital Selangor");

        // 5. Run the test based on the assignment scenario
        // Scenario: Emergency call for a Heart Attack at Location A
        System.out.println("=== SIMULATING EMERGENCY DISPATCH ===");
        graph.dispatchClosestAmbulance("Location A", availableAmbulances);
    }
}
