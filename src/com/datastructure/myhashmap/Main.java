package com.datastructure.myhashmap;

public class Main {
    public static void main(String[] args) {
        MyHashMap<String, Integer> map = new MyHashMap<>();
        map.put("one", 1);
        map.put("two", 2);


        System.out.println("Value for key 'one': " + map.get("one")); // Output: 1
        System.out.println("Value for key 'two': " + map.get("two")); // Output: 2

        map.remove("two");
        System.out.println("Value for key 'two' after removal: " + map.get("two")); // Output: null

        map.put("three", 3);
        map.put(null, 0);

        System.out.println("Value for key 'three': " + map.get("three")); // Output: 3
        System.out.println("Value for null key: " + map.get(null)); // Output: 0

        map.put("four", 4);
        map.put("five", 5);
        map.put("six", 6);
        System.out.println("Value for key 'one': " + map.get("one")); // Output: 1
        System.out.println("Value for key 'three': " + map.get("three")); // Output: 3
        System.out.println("Value for key 'four': " + map.get("four")); // Output: 4
        System.out.println("Value for key 'five': " + map.get("five")); // Output: 5
    }
}
