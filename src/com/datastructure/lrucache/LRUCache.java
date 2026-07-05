package com.datastructure.lrucache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @param <K>
 * @param <V>
 *
 * TODO: store value inside Node as well. In this manner there would not be need of another HashMap named "cache"
 *
 */
public class LRUCache<K,V> {

    private static final int MAX_CAPACITY = 2;

    private class Node {
        K key;
        Node prev;
        Node next;

        Node(K key){
            this.key = key;
        }
    }

    private final Map<K,V> cache;
    private final Map<K, Node> mapping;
    private final Node head, tail;

    private final ReentrantLock lock = new ReentrantLock();

    public LRUCache(){
        this.cache = new HashMap<>();
        this.mapping = new HashMap<>();
        this.head =  new Node(null);
        this.tail =  new Node(null);
        head.next = tail;
        tail.prev = head;
    }

    public void put(K key, V value){
        lock.lock();
        try {
            if (cache.containsKey(key)) {
                Node node = mapping.get(key);
                moveToHead(node);
            } else {
                /**
                 * When cache size equals to maximum capacity,
                 * evict the least recently used entry
                 */
                if (cache.size() == MAX_CAPACITY) {
                    Node nodeToRemove = removeFromTail();
                    mapping.remove(nodeToRemove.key);
                    cache.remove(nodeToRemove.key);
                }
                Node newNode = new Node(key);
                addToHead(newNode);
                mapping.put(key, newNode);
            }
            cache.put(key, value);
        } finally {
            lock.unlock();
        }
    }

    public V get(K key){
        lock.lock();
        try {
            if (cache.containsKey(key)) {
                V value = cache.get(key);
                /**
                 * Adjust LRU usage
                 * get the node reference from mapping
                 * remove the node from linkedlist
                 * add it into the head of linkedlist
                 */
                Node node = mapping.get(key);
                moveToHead(node);

                return value;
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    private void addToHead(Node node){
        node.next = head.next;
        node.next.prev = node;
        head.next = node;
        node.prev = head;
    }

    private void moveToHead(Node node){
        removeNode(node);
        addToHead(node);
    }

    private void removeNode(Node node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
        node.next = null;
        node.prev = null;
    }

    private Node removeFromTail(){
        Node nodeToRemove = tail.prev;
        removeNode(nodeToRemove);
        return nodeToRemove;
    }

}
