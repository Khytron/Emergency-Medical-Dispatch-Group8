/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.emergency_medical_dispatch;

import java.util.*;

public class Graph {
    // Adjacency list mapping a location to its connecting roads (edges)
    private Map<String, List<Edge>> cityMap;

    public Graph() {
        this.cityMap = new HashMap<>();
    }

    // Add new location (node) to the city
    public void addLocation(String location) {
        cityMap.putIfAbsent(location, new ArrayList<>());
    }

    // Add road (edge) between two locations with a specific ETA (weight)
    public void addRoad(String source, String destination, int eta) {
        // Ensure both locations exist in the map to prevent NullPointerException
        cityMap.putIfAbsent(source, new ArrayList<>());
        cityMap.putIfAbsent(destination, new ArrayList<>());
        
        cityMap.get(source).add(new Edge(destination, eta));
        cityMap.get(destination).add(new Edge(source, eta)); // Assuming two-way roads
    }

    // Get all locations in the city
    public Set<String> getLocations() {
        return cityMap.keySet();
    }

    /**
     * Dijkstra's Algorithm: Finds the closest available ambulance and prints the route.
     * @return The name of the ambulance dispatched, or null if none reachable.
     */
    public String dispatchClosestAmbulance(String emergencyLocation, List<String> availableAmbulances) {
        if (!cityMap.containsKey(emergencyLocation)) {
            System.out.println("Error: Location '" + emergencyLocation + "' not found on the map.");
            return null;
        }

        // Distance table to track the shortest known ETA to every location
        Map<String, Integer> shortestETA = new HashMap<>();
        // Tracks the path backwards to reconstruct the route
        Map<String, String> previousNode = new HashMap<>();
        // Priority Queue to always explore the closest locations first (Dijkstra)
        PriorityQueue<pathNode> minHeap = new PriorityQueue<>();

        // Initialize all distances to infinity, except the starting emergency location
        for (String node : cityMap.keySet()) {
            shortestETA.put(node, Integer.MAX_VALUE);
        }
        shortestETA.put(emergencyLocation, 0);
        minHeap.add(new pathNode(emergencyLocation, 0));

        while (!minHeap.isEmpty()) {
            pathNode current = minHeap.poll();
            String currentLocation = current.location;

            // If the current location has an available ambulance, we found the closest one
            if (availableAmbulances.contains(currentLocation)) {
                System.out.println("Closest ambulance found at: " + currentLocation);
                System.out.println("Estimated Time of Arrival (ETA): " + current.distance + " minutes");
                printRoute(currentLocation, emergencyLocation, previousNode);
                
                // Return the ambulance name so it can be marked as busy
                return currentLocation;
            }

            // Optimization: If we found a longer path than what we already know, skip it
            if (current.distance > shortestETA.get(currentLocation)) {
                continue;
            }

            // Explore all connecting roads from the current location
            if (cityMap.get(currentLocation) != null) {
                for (Edge road : cityMap.get(currentLocation)) {
                    int newETA = shortestETA.get(currentLocation) + road.weight;

                    // If we found a faster route to the neighbor, update it
                    if (newETA < shortestETA.get(road.destination)) {
                        shortestETA.put(road.destination, newETA);
                        previousNode.put(road.destination, currentLocation); // Remember how we got here
                        minHeap.add(new pathNode(road.destination, newETA));
                    }
                }
            }
        }
        System.out.println("No available ambulances can reach " + emergencyLocation);
        return null;
    }

    // Helper method to reconstruct and print the path
    private void printRoute(String start, String end, Map<String, String> previousNode) {
        List<String> route = new ArrayList<>();
        String step = start;
        
        // Trace backward from the ambulance to the emergency
        while (step != null) {
            route.add(step);
            step = previousNode.get(step);
        }
        
        System.out.println("Route: " + String.join(" -> ", route));
    }
}
