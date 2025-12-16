package com.klu.tfs.model;

import java.util.LinkedList;
import java.util.Queue;
import com.klu.tfs.exception.OverflowLaneException;

public class Lane {
	private String id;
    private Queue<Vehicle> queue;
    private int maxCapacity;

    public Lane(String id, int maxCapacity) {
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.queue = new LinkedList<>();
    }

    public synchronized boolean enqueue(Vehicle v) throws OverflowLaneException {
        if (queue.size() >= maxCapacity) {
            throw new OverflowLaneException("Lane " + id + " overflow");
        }
        return queue.add(v);
    }

    public synchronized Vehicle dequeue() {
        return queue.poll();
    }

    public synchronized int size() {
        return queue.size();
    }

    public String getId() {
        return id;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public synchronized Queue<Vehicle> getQueueSnapshot() {
        return new LinkedList<>(queue);
    }
}
