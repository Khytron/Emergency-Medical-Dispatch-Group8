/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.emergency_medical_dispatch;

public class Edge {
    String destination;
    int weight; // Represents ETA or distance

    public Edge(String destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }
}