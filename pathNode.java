/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package emergencydispatch;

public class pathNode implements Comparable<pathNode> {
    String location;
    int distance;

    public pathNode(String location, int distance) {
        this.location = location;
        this.distance = distance;
    }

    // This ensures the PriorityQueue always pulls the shortest distance first
    @Override
    public int compareTo(pathNode other) {
        return Integer.compare(this.distance, other.distance);
    }
}