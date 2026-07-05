package com.datastructure.lrucache;

public class Main {
    public static void main(String[] args) {
        LRUCache<Integer, String> lru = new LRUCache<>();
        lru.put(1,"one");
        lru.put(2,"two");

        System.out.println(lru.get(2));
        System.out.println(lru.get(1));

        lru.put(3, "Three");
        System.out.println(lru.get(1));
        System.out.println(lru.get(2));
        System.out.println(lru.get(3));
    }
}
