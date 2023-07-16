package com.oocourse.spec2.exceptions;

import java.util.HashMap;

public class MyEqualRelationException extends EqualRelationException {
    private int id1;
    private int id2;
    private static final Counter counter = new Counter();
    private static final HashMap<Integer, Integer> sumError = new HashMap<Integer, Integer>();

    public MyEqualRelationException(int id1, int id2) {
        this.id1 = id1;
        this.id2 = id2;
        if (!sumError.containsKey(id1)) {
            sumError.put(id1, 0);
        }

        if (!sumError.containsKey(id2)) {
            sumError.put(id2, 0);
        }
    }

    @Override
    public void print() {
        counter.increase();
        if (id1 == id2) {
            sumError.put(id1, sumError.get(id1) + 1);
        } else {
            sumError.put(id1, sumError.get(id1) + 1);
            sumError.put(id2, sumError.get(id2) + 1);
        }
        int tmp = 0;
        if (id1 > id2) {
            tmp = id1;
            id1 = id2;
            id2 = tmp;
        }
        System.out.println("er-" + counter.getCounter() + ", " + id1 + "-" + sumError.get(id1)
            + ", " + id2 + "-" + sumError.get(id2));
    }
}
