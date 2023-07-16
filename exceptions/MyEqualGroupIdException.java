package com.oocourse.spec2.exceptions;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private final int id;
    private static final Counter counter = new Counter();
    private static final HashMap<Integer, Integer> sumError = new HashMap<Integer, Integer>();

    public MyEqualGroupIdException(int id) {
        this.id = id;
        if (!sumError.containsKey(id)) {
            sumError.put(id, 0);
        }
    }

    @Override
    public void print() {
        counter.increase();
        sumError.put(id, sumError.get(id) + 1);
        System.out.println("egi-" + counter.getCounter() + ", "
            + id + "-" + sumError.get(id));
    }
}
