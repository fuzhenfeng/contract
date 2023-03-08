package org.contract.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Heap<E> {

    private List<E> data;

    private Comparator<E> comparator;

    public Heap(int capacity, Comparator<E> comparator){
        data = new ArrayList<>(capacity);
        this.comparator = comparator;
    }

    public Heap(Comparator<E> comparator){
        data = new ArrayList<>();
        this.comparator = comparator;
    }

    public Heap(E[] arr, Comparator<E> comparator){
        data = new ArrayList<>(Arrays.asList(arr));
        this.comparator = comparator;
        if(arr.length != 1){
            for(int i = parent(arr.length - 1) ; i >= 0 ; i --)
                siftDown(i);
        }
    }

    public int size(){
        return data.size();
    }

    public boolean isEmpty(){
        return data.isEmpty();
    }

    private int parent(int index){
        if(index == 0)
            throw new IllegalArgumentException();
        return (index - 1) / 2;
    }

    private int leftChild(int index){
        return index * 2 + 1;
    }

    private int rightChild(int index){
        return index * 2 + 2;
    }

    public void add(E e){
        data.add(data.size(), e);
        siftUp(data.size() - 1);
    }

    private void siftUp(int k){
        while(k > 0 && comparator.compare(data.get(parent(k)), data.get(k)) < 0 ){
            // 则交换k和父节点
            swap(k, parent(k));
            // 设置k为父节点
            k = parent(k);
        }
    }

    public E findMax(){
        if(data.size() == 0)
            throw new IllegalArgumentException();
        return data.get(0);
    }

    public E extractMax(){
        E ret = findMax();
        swap(0, data.size() - 1);
        data.remove(data.size() - 1);
        siftDown(0);
        return ret;
    }

    private void siftDown(int k){
        while(leftChild(k) < data.size()){
            int j = leftChild(k);
            if( j + 1 < data.size() &&
                    comparator.compare(data.get(j + 1), data.get(j)) > 0 ){
                j = rightChild(k);
            }

            if(comparator.compare(data.get(k), data.get(j)) >= 0 )
                break;
            swap(k, j);
            k = j;
        }
    }

    public E replace(E e){
        E ret = findMax();
        data.set(0, e);
        siftDown(0);
        return ret;
    }

    private void swap(int i, int j){
        if(i < 0 || i >= data.size() || j < 0 || j >= data.size())
            throw new IllegalArgumentException();
        E t = data.get(i);
        data.set(i, data.get(j));
        data.set(j, t);
    }
}
