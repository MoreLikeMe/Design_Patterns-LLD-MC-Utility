package com.datastructure.myhashmap;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unchecked")
public class MyHashMap<K,V> {

    class Node {
        final K key;
        V value;
        Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final static int DEFAULT_CAPACITY = 4;
    private final static float LOAD_FACTOR = 0.75f;

    private volatile Object[] bucket;
    private volatile int capacity;
    private final AtomicInteger size;
    private volatile V valueForNullKey;

    //Locks
    private final ReadWriteLock resizeLock = new ReentrantReadWriteLock();
    private volatile Object[] locks;

    public MyHashMap(){
        this.capacity = DEFAULT_CAPACITY;
        this.bucket = new Object[capacity];
        this.size = new AtomicInteger(0);
        this.locks = new Object[capacity];
        for(int i=0;i<capacity;i++){
            locks[i] = new Object();
        }
    }

    public int size(){
        return size.get();
    }

    public void put(K key, V value){
        if(key == null) {
            synchronized (this) {
                valueForNullKey = value;
                size.incrementAndGet();
            }
            return;
        }

        resizeLock.readLock().lock();
        try {
            int bucketIndex = getBucketIndex(key);
            Object lock = locks[bucketIndex];
            synchronized (lock) {
                Node node = (Node) bucket[bucketIndex];

                while (node != null) {
                    if (node.key.equals(key)) {
                        node.value = value;
                        return;
                    }
                    node = node.next;
                }

                Node newNode = new Node(key, value);
                newNode.next = (Node) bucket[bucketIndex];
                bucket[bucketIndex] = newNode;
                size.incrementAndGet();
            }
        }
        finally {
            resizeLock.readLock().unlock();
        }

        //When size becomes greater than or equal to capacity * LOAD_FACTOR,
        // we need to resize the bucket array to maintain the load factor.
        if (size() >= capacity * LOAD_FACTOR) {
            resize();
        }
    }

    public void remove(K key){
        if(key == null) {
            synchronized (this) {
                valueForNullKey = null;
                size.decrementAndGet();
            }
            return;
        }

        resizeLock.readLock().lock();
        try {
            int bucketIndex = getBucketIndex(key);
            Object lock = locks[bucketIndex];
            synchronized (lock) {
                Node node = (Node) bucket[bucketIndex];
                Node prev = null;

                while (node != null) {
                    if (node.key.equals(key)) {
                        if (prev == null) {
                            bucket[bucketIndex] = node.next;
                        } else {
                            prev.next = node.next;
                        }
                        node.next = null;
                        size.decrementAndGet();
                        return;
                    }
                    prev = node;
                    node = node.next;
                }
            }
        }
        finally{
            resizeLock.readLock().unlock();
        }
    }

    public V get(K key){
        if(key == null) {
            synchronized (this) {
                return valueForNullKey;
            }
        }

        resizeLock.readLock().lock();
        try {
            int bucketIndex = getBucketIndex(key);
            Object lock = locks[bucketIndex];
            synchronized (lock) {
                Node node = (Node) bucket[bucketIndex];

                while (node != null) {
                    if (node.key.equals(key)) {
                        return node.value;
                    }
                    node = node.next;
                }
                return null;
            }
        }
        finally {
            resizeLock.readLock().unlock();
        }
    }


    private void resize(){

        if (resizeLock.writeLock().tryLock()) {
            try {
                if (size() < capacity * LOAD_FACTOR) {
                    return; // Another thread might have resized the map, so we check again
                }
                System.out.println("Current Capacity:" + capacity + ", New Capacity: " + capacity * 2);
                int newCapacity = capacity * 2;
                Object[] newBucket = new Object[newCapacity];
                Object[] newLock = new Object[newCapacity];
                for(int i=0;i<newCapacity;i++) {
                    newLock[i] = new Object();
                }

                for (int i = 0; i < capacity; i++) {
                    if (bucket[i] == null) continue;
                    Node oldNode = (Node) bucket[i];
                    while (oldNode != null) {
                        int hash = oldNode.key.hashCode();
                        hash = hash ^ (hash >>> 16);
                        int bucketIndex = hash & (newCapacity - 1);
                        Node node = (Node) newBucket[bucketIndex];
                        Node newNode = new Node(oldNode.key, oldNode.value);
                        newNode.next = node;
                        newBucket[bucketIndex] = newNode;

                        oldNode = oldNode.next;
                    }
                }
                this.bucket = newBucket;
                this.capacity = newCapacity;
                this.locks = newLock;
            } finally {
                resizeLock.writeLock().unlock();
            }
        }
    }

    private int getBucketIndex(K key) {
        int hash = key.hashCode();
        hash = hash ^ (hash >>> 16); //spread bytes to reduce collisions
        return hash & (capacity - 1); //use bitwise AND to get index within capacity
    }

}
