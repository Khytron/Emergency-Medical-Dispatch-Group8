/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.emergency_medical_dispatch;

/**
 *
 * @author Zai Husna
 */
    public class Queue<E> {
    private E[] data;
    private int size;
    private int capacity;
    private int head;
    private int tail;

   
  
    public Queue() {
        this.capacity = 10;
        this.data = (E[]) new Object[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }


 
    public Queue(int initial) {
        this.capacity = initial;
        this.data = (E[]) new Object[capacity];
        this.size = 0;
        this.head = 0;
        this.tail = 0;
    }


    public void enqueue(E e) {
        if (size == capacity) {
            resize();
        }
        data[tail] = e;
        tail = (tail + 1) % capacity; 
        size++;
    }

   
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

