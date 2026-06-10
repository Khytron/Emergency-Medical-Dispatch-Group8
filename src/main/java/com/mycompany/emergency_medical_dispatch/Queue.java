/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.emergency_medical_dispatch;

/**
 *
 * @author Zai Husna
 */
/**
 * Custom Queue implementation for non-urgent emergency calls (FIFO).
 * This class uses a circular array with dynamic resizing to handle 
 * an increasing number of calls efficiently.
 * 
 * @param <E> The type of elements stored in the queue.
 */
public class Queue<E> {
    private E[] data;
    private int size;
    private int capacity;
    private int head;
    private int tail;

    /**
     * Default constructor with initial capacity of 10.
     */
    public Queue() {
        this.capacity = 10;
        this.data = (E[]) new Object[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    /**
     * Constructor with custom initial capacity.
     */
    public Queue(int initial) {
        this.capacity = initial;
        this.data = (E[]) new Object[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }

    /**
     * Adds an element to the back of the queue (FIFO).
     * Automatically resizes if the capacity is reached.
     */
    public void enqueue(E e) {
        if (size == capacity) {
            resize();
        }
        data[tail] = e;
        tail = (tail + 1) % capacity;
        /*
         simplified logic: when tail index reach last index (capacity),
         tail index will become zero, so it loops back to the first index (assuming first index in dequeued)
         modulo operator (%) makes this possible by giving the remainder: 0.1, 0.2, ... 0.9, 1.0
         give 1,2,3... if tail /= capacity
         give 0 if tail = capacity
        */
        size++;
    }

    /**
     * Removes and returns the element from the front of the queue.
     */
    public E dequeue() {
        if (isEmpty()) {
            return null;
        }
        E element = data[head];
        data[head] = null;
        head = (head + 1) % capacity; 
        size--;
        return element;
    }

    public E getElement() {
        if (isEmpty()) {
            return null;
        }
        return data[head];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    /**
     * Doubles the capacity of the circular array when full.
     */
    public void resize() {
        int newCapacity = capacity * 2;
        E[] newData = (E[]) new Object[newCapacity];
        
        for (int i = 0; i < size; i++) {
            newData[i] = data[(head + i) % capacity];
        }
        
        data = newData;
        head = 0;
        tail = size;
        capacity = newCapacity;
    }
}

