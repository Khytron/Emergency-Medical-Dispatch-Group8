package com.mycompany.emergency_medical_dispatch;

import java.util.*;
import java.util.Scanner; 
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClass {
    public static void main(String[] args) {
        // 1. Initialize scanner variable
        Scanner scanner = new Scanner(System.in);
        // 2. Initialize all Data Structures
        Graph cityMap = new Graph();
        PriorityQueueClass urgentCalls = new PriorityQueueClass();
        Queue<Call> nonUrgentCalls = new Queue<>(); 
        
        // 3. Build the Map (Graph Integration)
        cityMap.addLocation("Shah Alam");
        cityMap.addLocation("Elmina");
        cityMap.addLocation("Batu Caves");
        cityMap.addLocation("Universiti Malaya Medical Centre");
        cityMap.addLocation("Subang Jaya");
        cityMap.addLocation("Putrajaya");
        cityMap.addLocation("Hospital Tengku Ampuan Rahimah");
        cityMap.addLocation("Taman Sentosa");
        cityMap.addLocation("Klang");
        cityMap.addLocation("Ara Damansara Medical Centre");
        
        // Weighted Graph Values
        cityMap.addRoad("Klang", "Hospital Tengku Ampuan Rahimah", 3);
        cityMap.addRoad("Klang", "Taman Sentosa", 9);
        cityMap.addRoad("Klang", "Shah Alam", 12);
        cityMap.addRoad("Klang", "Ara Damansara Medical Centre", 27);
        cityMap.addRoad("Ara Damansara Medical Centre", "Elmina", 21);
        cityMap.addRoad("Elmina", "Shah Alam", 24);
        cityMap.addRoad("Shah Alam", "Hospital Tengku Ampuan Rahimah", 15);
        cityMap.addRoad("Shah Alam", "Universiti Malaya Medical Centre", 18);
        cityMap.addRoad("Hospital Tengku Ampuan Rahimah", "Taman Sentosa", 8);
        cityMap.addRoad("Hospital Tengku Ampuan Rahimah", "Subang Jaya", 26);
        cityMap.addRoad("Taman Sentosa", "Subang Jaya", 18);
        cityMap.addRoad("Subang Jaya", "Universiti Malaya Medical Centre", 16);
        cityMap.addRoad("Subang Jaya", "Putrajaya", 29);
        cityMap.addRoad("Putrajaya", "Universiti Malaya Medical Centre", 36);
        cityMap.addRoad("Universiti Malaya Medical Centre", "Batu Caves", 19);
        cityMap.addRoad("Batu Caves", "Elmina", 26);
        
        
        // Track available ambulances
        List<String> availableAmbulances = new ArrayList<>();
        availableAmbulances.add("Universiti Malaya Medical Centre");
        availableAmbulances.add("Hospital Tengku Ampuan Rahimah");
        availableAmbulances.add("Ara Damansara Medical Centre");

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
        int dispatchCount = 0;

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
                    dispatchCount++;
                    System.out.println("Status: Ambulance dispatched from " + dispatchedAmbulance + ". (Ambulance is now BUSY)");
                } else {
                    System.out.println("Status: FAILED. No available ambulance can reach this location.");
                }
                System.out.println("------------------------------------------------------");
            }
        }
        
        sleep(1000); // Wait 1 second
        System.out.println("\n=== ALL EMERGENCY CALLS PROCESSED ===");
        System.out.println("AMOUNT OF AMBULANCE DISPATCHED: " + dispatchCount);
        scanner.close(); 
    }
    
    // Method for pausing application for X miliseconds
    static void sleep(int ms){
        try {  
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
