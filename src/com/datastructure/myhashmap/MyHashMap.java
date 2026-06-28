package com.datastructure.myhashmap;

@SuppressWarnings("unchecked")
public class MyHashMap<K,V> {

    class Node {
        K key;
        V value;
        Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private final static int DEFAULT_SIZE = 16;
    private final static float LOAD_FACTOR = 0.75f;

    private Object[] bucket;
    private int capacity;
    private int size;
    private V valueForNullKey;

    public MyHashMap(){
        this.capacity = DEFAULT_SIZE;
        this.bucket = new Object[capacity];
    }

    public MyHashMap(int initialCapacity){
        this.capacity = initialCapacity;
        this.bucket = new Object[capacity];
    }

    public int size(){
        return size;
    }

    public void put(K key, V value){
        if(key == null) {
            valueForNullKey = value;
            size++;
            return;
        }

        int bucketIndex = getBucketIndex(key);
        Node node = (Node) bucket[bucketIndex];

        while(node != null){
            if(node.key.equals(key)){
                node.value = value;
                return;
            }
            node = node.next;
        }

        Node newNode = new Node(key, value);
        newNode.next = (Node) bucket[bucketIndex];
        bucket[bucketIndex] = newNode;
        size++;

        //When size becomes greater than or equal to capacity * LOAD_FACTOR,
        // we need to resize the bucket array to maintain the load factor.
        if(size() >= capacity * LOAD_FACTOR){
            resize();
        }
    }

    public void remove(K key){
        if(key == null) {
            valueForNullKey = null;
            size--;
            return;
        }

        int bucketIndex = getBucketIndex(key);
        Node node = (Node) bucket[bucketIndex];
        Node prev = null;

        while(node != null){
            if(node.key.equals(key)){
                if(prev == null){
                    bucket[bucketIndex] = node.next;
                } else {
                    prev.next = node.next;
                }
                node.next = null;
                size--;
                return;
            }
            prev = node;
            node = node.next;
        }
    }

    public V get(K key){
        if(key == null) {
            return valueForNullKey;
        }

        int bucketIndex = getBucketIndex(key);
        Node node = (Node) bucket[bucketIndex];

        while(node != null){
            if(node.key.equals(key)){
                return node.value;
            }
            node = node.next;
        }

        return null;
    }


    private void resize(){
        System.out.println("Current Capacity:" + capacity + ", New Capacity: " + capacity*2);
        int newCapacity = capacity * 2;
        Object[] newBucket = new Object[newCapacity];

        for(int i=0;i<capacity;i++){
            if(bucket[i]==null) continue;
            Node oldNode = (Node) bucket[i];
            while(oldNode!=null){
                int bucketIndex = oldNode.key.hashCode() % newCapacity;
                Node node = (Node) newBucket[bucketIndex];
                Node newNode = new Node(oldNode.key, oldNode.value);
                newNode.next = node;
                newBucket[bucketIndex] = newNode;

                oldNode = oldNode.next;
            }
        }
        this.bucket = newBucket;
        this.capacity = newCapacity;
    }

    private int getBucketIndex(K key) {
        return key.hashCode() % capacity;
    }

}
