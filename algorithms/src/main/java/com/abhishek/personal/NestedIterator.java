package com.abhishek.personal;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class NestedIterator implements Iterator<Integer> {

    Queue<Integer> flat;
    List<NestedInteger> nestedList;

    public NestedIterator(List<NestedInteger> nestedList) {
        this.nestedList = nestedList;
        flat = new LinkedList();
        nestedList.stream().forEach(x->{
            if(x.isInteger())
            {
                flat.add(x.getInteger());
            }
            else
            {
                x.getList().stream().forEach(y->{
                    flat.add(y.getInteger());
                });
            }
        });

    }

    public Integer next() {
        return flat.poll();

    }

    public void remove() {

    }

    public boolean hasNext() {
        return flat.isEmpty();

    }
}
