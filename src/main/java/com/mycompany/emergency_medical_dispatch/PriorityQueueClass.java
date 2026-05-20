package com.mycompany.emergency_medical_dispatch;

import java.util.PriorityQueue;
import java.util.Comparator;

public class PriorityQueueClass {
        // Min-Heap bcs smallest number get priority
        // The priority queue - severity 1 comes out first
        private PriorityQueue<Call> pq;

        // Constructor
        public PriorityQueueClass() {
            pq = new PriorityQueue<>(Comparator.comparingInt(call -> call.severity));
        }

        // Add a call to the queue
        public void insert(Call call) {
            pq.add(call);
            System.out.println("Added to Priority Queue: " + call.description + " (Severity " + call.severity + ")");
        }

        // Remove and return the highest priority call
        public Call extractMin() {
            Call next = pq.poll(); // poll removes, peek look but deosnt remove
            if (next != null) {
                System.out.println("Dispatching: " + next.description + " (Severity " + next.severity + ")");
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
