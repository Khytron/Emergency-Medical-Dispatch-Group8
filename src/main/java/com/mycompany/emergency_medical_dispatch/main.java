package com.mycompany.emergency_medical_dispatch;

import java.util.*;

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
        
        // 3. Simulate the exact scenario from project 
        // Format: Call(severity, description, location)
        urgentCalls.insert(new Call(1, "Heart attack", "Location A"));
        nonUrgentCalls.enqueue(new Call(3, "Minor car accident", "Location B")); 
        urgentCalls.insert(new Call(2, "House fire", "Location C"));

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

            // Route the ambulance using pthfinding gaph
            if (currentEmergency != null) {
                // Pass the location directly to Dijkstra algorithm
                cityMap.dispatchClosestAmbulance(currentEmergency.location, availableAmbulances);
                System.out.println("------------------------------------------------------");
            }
        }
    }
}
