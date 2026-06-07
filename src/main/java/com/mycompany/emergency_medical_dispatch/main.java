package com.mycompany.emergency_medical_dispatch;

import java.util.*;
import java.util.Scanner; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        // 1. Initialize scanner variable
        Scanner scanner = new Scanner(System.in);
        // 2. Initialize all Data Structures
        Graph cityMap = new Graph();
        PriorityQueueClass urgentCalls = new PriorityQueueClass();
        Queue<Call> nonUrgentCalls = new Queue<>(); 
        
        // 3. Build the Map (Graph Integration)
        cityMap.addLocation("Location A");
        cityMap.addLocation("Location B");
        cityMap.addLocation("Location C");
        cityMap.addLocation("Location D");
        cityMap.addLocation("Hospital Kuala Lumpur");
        cityMap.addLocation("Hospital Selangor");

        cityMap.addRoad("Hospital Kuala Lumpur", "Location C", 7);
        cityMap.addRoad("Location C", "Location A", 3);
        cityMap.addRoad("Hospital Selangor", "Location A", 10);
        cityMap.addRoad("Location B", "Hospital Selangor", 4);
        cityMap.addRoad("Location D", "Location B", 4);
        cityMap.addRoad("Location D", "Hospital Kuala Lumpur", 3);
        

        // Track available ambulances
        List<String> availableAmbulances = new ArrayList<>();
        availableAmbulances.add("Hospital Kuala Lumpur");
        availableAmbulances.add("Hospital Selangor");

        System.out.println("=== RECEIVING INCOMING EMERGENCY CALLS ===");
        
        // 4. User's input
        System.out.println("Valid locations: " + cityMap.getLocations());
        System.out.println("Please describe your Emergency Type (eg. Heart Attack)");

        int callCount = 1;
        while (true) {
            System.out.println("\n--- Emergency Call " + callCount + " ---");
            
            String type = "";
            while (type.isEmpty()) {
                System.out.println("Enter Emergency Type (or 'exit' if done): ");
                type = scanner.nextLine().trim();
                if (type.equalsIgnoreCase("exit")) break;
                if (type.isEmpty()) {
                    System.out.println("Emergency type cannot be empty. Please describe the situation.");
                }
            }
            if (type.equalsIgnoreCase("exit")) break;
            
            int severity = 0;
            while (true) {
                System.out.println("Enter Severity (1 = Highest, 2 = Medium, 3 = Lowest): ");
                String severityInput = scanner.nextLine().trim();
                try {
                    severity = Integer.parseInt(severityInput);
                    if (severity >= 1 && severity <= 3) break;
                    else System.out.println("Invalid severity! Please enter 1, 2, or 3.");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a number (1, 2, or 3).");
                }
            }
            
            String location = "";
            while (true) {
                System.out.println("Enter Location: ");
                location = scanner.nextLine().trim();
                if (cityMap.getLocations().contains(location)) break;
                else {
                    System.out.println("Invalid location! Please choose from: " + cityMap.getLocations());
                }
            }
            
            // Automatically sort into the correct Queue based on severity
            if (severity == 3) {
                nonUrgentCalls.enqueue(new Call(severity, type, location));
                System.out.println("Added to Standard Queue: " + type + " (Severity " + severity + ")");
            } else {
                urgentCalls.insert(new Call(severity, type, location));
            }
            callCount++;
        }
        
        

        System.out.println("\n=== STARTING SYSTEM DISPATCH WORKFLOW ===");

        // 5. Main Integration Loop (Fulfills rubrics for System Integration & Workflow)
        // This loop processes calls based on priority and handles dynamic ambulance availability.
        List<String> busyAmbulances = new ArrayList<>();

        while (!urgentCalls.isEmpty() || !nonUrgentCalls.isEmpty()) {
            // Wait for 1 seconds to simulate program execution
            sleep(1000);
            
            // If all ambulances are busy, we must wait for one to become free
            if (availableAmbulances.isEmpty()) {
                System.out.println("\n[SYSTEM NOTICE] All ambulances are currently busy.");
                System.out.println("Remaining calls are waiting in their respective queues...");
                
                // Simulate an ambulance finishing its mission and returning to service
                String freedAmbulance = busyAmbulances.remove(0);
                availableAmbulances.add(freedAmbulance);
                System.out.println("[SYSTEM UPDATE] Ambulance at " + freedAmbulance + " is now FREE and returning to base.");
                System.out.println("------------------------------------------------------");
                continue; // Re-evaluate the queues with the newly available ambulance
            }

            Call currentEmergency = null;

            // Rule: Priority Queue (Severity 1-2) always goes first before Standard Queue (Severity 3)
            if (!urgentCalls.isEmpty()) {
                currentEmergency = urgentCalls.extractMin(); // Pulls the most severe call
            } else if (!nonUrgentCalls.isEmpty()) {
                currentEmergency = nonUrgentCalls.dequeue(); // Pulls the oldest non-urgent call
            }

            // Route the ambulance using Dijkstra's algorithm on the city graph
            if (currentEmergency != null) {
                System.out.println("\nProcessing: " + currentEmergency);
                // Pass the location directly to Dijkstra algorithm
                String dispatchedAmbulance = cityMap.dispatchClosestAmbulance(currentEmergency.location, availableAmbulances);
                
                if (dispatchedAmbulance != null) {
                    // Mark as busy
                    availableAmbulances.remove(dispatchedAmbulance);
                    busyAmbulances.add(dispatchedAmbulance);
                    System.out.println("Status: Ambulance dispatched from " + dispatchedAmbulance + ". (Ambulance is now BUSY)");
                } else {
                    System.out.println("Status: FAILED. No available ambulance can reach this location.");
                }
                System.out.println("------------------------------------------------------");
            }
        }
        
        System.out.println("\n=== ALL EMERGENCY CALLS PROCESSED ===");
        scanner.close(); 
    }
    
    static void sleep(int ms){
        try {  
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
