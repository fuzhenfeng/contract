package org.contract.cache;

import org.contract.common.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class LocalCache implements Cache {
    int size;
    int capacity;
    Map<String, Node> map;
    Node head, tail;

    public LocalCache(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.map = new HashMap<>();
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public void put(String key, String value) {
        Node node = map.get(key);
        if (node == null) {
            Node newNode = new Node();
            newNode.key = key;
            newNode.value = value;

            map.put(key, newNode);
            add(newNode);
            size++;

            if (size > capacity) {
                Node tail = popTail();
                map.remove(tail.key);
                size--;
            }
        } else {
            node.value = value;
            moveToHead(node);
        }
    }

    @Override
    public String get(String key) {
        Node node = map.get(key);
        if (node == null) {
            return StringUtils.EMPTY;
        } else {
            moveToHead(node);
            return node.value;
        }

    }

    private void add(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    private void remove(Node node) {
        Node prev = node.prev;
        Node next = node.next;
        prev.next = next;
        next.prev = prev;
    }

    private void moveToHead(Node node) {
        remove(node);
        add(node);
    }

    private Node popTail() {
        Node res = tail.prev;
        remove(res);
        return res;
    }

    class Node {
        String key, value;
        Node prev, next;
    }
}
