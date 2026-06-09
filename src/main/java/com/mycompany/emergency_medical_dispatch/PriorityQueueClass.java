package com.mycompany.emergency_medical_dispatch;

import java.util.PriorityQueue;
import java.util.Comparator;

/**
 * PriorityQueueClass implements a severity-based prioritization system.
 * It functions as a Max-Heap based on priority (where lower severity value = higher priority).
 * It uses a Min-Heap internally on the severity field to ensure that 
 * Severity 1 is always processed before 2 and 3.
 * 
 * Complexity:
 * - Insertion: O(log n)
 * - Removal (extractMin): O(log n)
 */
public class PriorityQueueClass {
        // Min-Heap because the smallest severity number gets the highest priority
        private PriorityQueue<Call> pq;

        public PriorityQueueClass() {
            // Using a comparator to order calls by their severity field
            pq = new PriorityQueue<>(Comparator.comparingInt(call -> call.severity));
        }

        /**
         * Adds a call to the heap in O(log n) time.
         */
        public void insert(Call call) {
            pq.add(call);
            String sevLabel = "Low";
            if (call.severity == 1) sevLabel = "Critical";
            else if (call.severity == 2) sevLabel = "Medium";
            
            System.out.println("Added to Priority Queue: " + call.description + " at " + call.location + " (Severity " + sevLabel + ")");
        }

        /**
         * Removes and returns the call with the highest priority in O(log n) time.
         */
        public Call extractMin() {
            Call next = pq.poll(); // poll removes, peek look but doesn't remove
            if (next != null) {
                System.out.println("Extracting from PQ: " + next.description + " (Severity " + next.severity + ")");
            }
            return next;
        }

        // Check if queue is empty
        public boolean isEmpty() {
            return pq.isEmpty();
        }

        // See how many calls are waiting
        public int size() {
            return pq.size();
        }
}
