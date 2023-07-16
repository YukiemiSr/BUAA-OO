package com.oocourse.spec3.exceptions;

import java.util.HashMap;

public class MyMessageIdNotFoundException extends MessageIdNotFoundException {
    private final int id;
    private static final Counter counter = new Counter();
    private static final HashMap<Integer, Integer> sumError = new HashMap<Integer, Integer>();

    public MyMessageIdNotFoundException(int id) {
        this.id = id;
        if (!sumError.containsKey(id)) {
            sumError.put(id, 0);
        }
    }

    @Override
    public void print() {
        counter.increase();
        sumError.put(id, sumError.get(id) + 1);
        System.out.println("minf-" + counter.getCounter() + ", "
            + id + "-" + sumError.get(id));
    }
}
