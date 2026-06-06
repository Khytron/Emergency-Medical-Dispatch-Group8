package com.mycompany.emergency_medical_dispatch;

import java.util.*;
import java.util.Scanner; 

public class Main {
    public static void main(String[] args) {
        // 1. Initialize all Data Structures
        Graph cityMap = new Graph();
        PriorityQueueClass urgentCalls = new PriorityQueueClass();
        Queue<Call> nonUrgentCalls = new Queue<>(); 
        
        // 2. Build the Map (Graph Integration)
        cityMap.addLocation("Location A");
        cityMap.addLocation("Location B");
        cityMap.addLocation("Location C");
        cityMap.addLocation("Hospital Kuala Lumpur");
        cityMap.addLocation("Hospital Selangor");

        cityMap.addRoad("Hospital Kuala Lumpur", "Location C", 7);
        cityMap.addRoad("Location C", "Location A", 3);
        cityMap.addRoad("Hospital Selangor", "Location B", 4);
        cityMap.addRoad("Location B", "Location A", 10);

        // Track available ambulances
        List<String> availableAmbulances = new ArrayList<>();
        availableAmbulances.add("Hospital Kuala Lumpur");
        availableAmbulances.add("Hospital Selangor");

        System.out.println("=== RECEIVING INCOMING EMERGENCY CALLS ===");
        
        // 3. User's input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the details for incoming emergencies.");

        for (int i = 1; i <= 3; i++) {
            System.out.println("\n--- Emergency Call " + i + " ---");
            
            System.out.print("Enter Emergency Type: ");
            String type = scanner.nextLine();
            
            System.out.print("Enter Severity (1 = Highest, 2 = Medium, 3 = Lowest): ");
            int severity = scanner.nextInt();
            scanner.nextLine(); 
            
            System.out.print("Enter Location: ");
            String location = scanner.nextLine();
            
            // Automatically sort into the correct Queue based on severity
            if (severity == 3) {
                nonUrgentCalls.enqueue(new Call(severity, type, location));
                System.out.println("Added to Standard Queue: " + type + " (Severity " + severity + ")");
            } else {
                urgentCalls.insert(new Call(severity, type, location));
                
            }
        }
        
        scanner.close(); 

        System.out.println("\n=== STARTING SYSTEM DISPATCH WORKFLOW ===");

        // 4. Main Integration Loop
        while (!urgentCalls.isEmpty() || !nonUrgentCalls.isEmpty()) {
            
            // Check if we have ambulances to send
            if (availableAmbulances.isEmpty()) {
                System.out.println("All ambulances are currently busy. Remaining calls are waiting in queue...");
                break; 
            }

            Call currentEmergency = null;

            //Priority Queue always goes first
            if (!urgentCalls.isEmpty()) {
                currentEmergency = urgentCalls.extractMin(); // Pulls the most severe call
            } else if (!nonUrgentCalls.isEmpty()) {
                currentEmergency = nonUrgentCalls.dequeue(); // Pulls the oldest non-urgent call
            }

            // Route the ambulance using pathfinding graph
            if (currentEmergency != null) {
                // Pass the location directly to Dijkstra algorithm
                cityMap.dispatchClosestAmbulance(currentEmergency.location, availableAmbulances);
                System.out.println("------------------------------------------------------");
            }
        }
    }
}
