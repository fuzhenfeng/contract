package org.contract.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoopQueue<E> {
    private final static Logger log = LogManager.getLogger(LoopQueue.class);

    private E[] data;
    private int front, tail;
    private int size;
    private int max;

    public LoopQueue(int capacity, int maxSize){
        data = (E[])new Object[capacity + 1];
        this.max = maxSize;
        front = 0;
        tail = 0;
        size = 0;
    }

    public int getCapacity(){
        return data.length - 1;
    }

    public boolean isEmpty(){
        return front == tail;
    }

    public int getSize(){
        return size;
    }

    public void enqueue(E e) throws RunException {
        if((tail + 1) % data.length == front) {
            int capacity = getCapacity() * 2;
            if(capacity > max) {
                log.error("queue size exceeds maximum");
                throw new RunException();
            }
            resize(capacity);
        }
        data[tail] = e;
        tail = (tail + 1) % data.length;
        size ++;
    }

    public E dequeue(){
        if(isEmpty()) {
            throw new IllegalArgumentException();
        }
        E ret = data[front];
        data[front] = null;
        front = (front + 1) % data.length;
        size --;
        if(size == getCapacity() / 4 && getCapacity() / 2 != 0) {
            resize(getCapacity() / 2);
        }
        return ret;
    }

    public E getFront(){
        if(isEmpty()) {
            throw new IllegalArgumentException();
        }
        return data[front];
    }

    public boolean canResize(){
        return getCapacity() * 2 > max;
    }

    private void resize(int newCapacity){
        E[] newData = (E[])new Object[newCapacity + 1];
        for(int i = 0 ; i < size ; i ++)
            newData[i] = data[(i + front) % data.length];

        data = newData;
        front = 0;
        tail = size;
    }
}
