package simpledb.buffer;

import java.util.*;

class LRUCache<K,V> {

    private int capacity;
    private int size = 0;
    DoubleLikedList<Node> cache = new DoubleLikedList<>();
    Map<K, Node> cacheIndex = new HashMap<>();

    public LRUCache(int capacity) {
        this.capacity = capacity;
    }

    public Optional<V> get(K key) {
        Node value = cacheIndex.getOrDefault(key,null);
        if(value!=null){
            cache.unlink(value);
            // add value to the top of linkedlist
            cache.add(value);
        }
        return value!=null?Optional.of(value.value):Optional.empty();
    }

    public Optional<V> getLRUNode(){
        if(size == 0)
            return Optional.empty();
        Node node = cache.tail;
        cache.unlink(node);
        cacheIndex.remove(node.key);
        size--;
        return Optional.of(node.value);
    }

    public void put(K key, V value) {
        Node newElement = new Node(key, value);
        Node existingValue = cacheIndex.getOrDefault(key,null);
        if(existingValue!=null){
            // If exits
            // remove it from its current position in ll
            cache.unlink(existingValue);

            // add value to the top of linkedlist
            cache.add(newElement);

            // update the cacheIndex(HashMap)
            cacheIndex.put(key,newElement);
        }else{
            //If not exits verify the capacity of the ll
            if(capacity>size){
                // Add new value to the top of linkedlist
                cache.add(newElement);
                // Add new value to the hashmap
                cacheIndex.put(key, newElement);
                // increment size
                size++;
            }else{
                //Evict/Remove last element of the linkedlist
                Node lastElement = cache.tail;
                cache.unlink(lastElement);
                System.out.println("Element evicted "+ lastElement);
                //Evict/Remove element from the hashmap
                cacheIndex.remove(lastElement.key);
                // add new element to the top of the list
                cache.add(newElement);
                // add element to the hashmap
                cacheIndex.put(key,newElement);
            }
        }
    }



    private class Node {
        K key;
        V value;
        Node prev;
        Node next;
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            Node event = (Node) o;
            return key == event.key && value == event.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }

        @Override
        public String toString() {
            return "Event{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.prev = null;
            this.next = null;
        }

        public Node(K key, V value, Node prev, Node next) {
            this.key = key;
            this.value = value;
            this.prev = prev;
            this.next = next;
        }
    }

    class DoubleLikedList<E extends Node>{
        E head;
        E tail;

        public void add(E newNode){
            if(head != null){
                // The previous element of head should be the newNode
                head.prev = newNode;
                //The next element of newNode should be head
                newNode.next = head;
            }

            if(tail == null){
                tail = newNode;
            }

            this.head = newNode;
        }

        public void unlink(E node){
            // get previous Node
            E previousNode = (E) node.prev;
            // get next Node
            E nextNode = (E) node.next;

            if(previousNode!=null)
                previousNode.next = nextNode;

            if(nextNode!=null)
                nextNode.prev = previousNode;

            if(node == head){
                head = nextNode;
            }
            if(node == tail){
                tail = previousNode;
            }



            node.prev = null;
            node.next = null;
        }

    }

}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */

