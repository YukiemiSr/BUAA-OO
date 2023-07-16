package com.oocourse.spec3.exceptions;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private int id1;
    private int id2;
    private static final Counter COUNTER = new Counter();
    private static final HashMap<Integer, Integer> sumError = new HashMap<Integer, Integer>();

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id2, id1);
        if (!sumError.containsKey(id1)) {
            sumError.put(id1, 0);
        }

        if (!sumError.containsKey(id2)) {
            sumError.put(id2, 0);
        }
    }

    @Override
    public void print() {
        COUNTER.increase();
        sumError.put(id1, sumError.get(id1) + 1);
        sumError.put(id2, sumError.get(id2) + 1);
        int tmp = 0;
        if (id1 > id2) {
            tmp = id1;
            id1 = id2;
            id2 = tmp;
        }
        System.out.println("rnf-" + COUNTER.getCounter() + ", " + id1 + "-"
            + sumError.get(id1) + ", " + id2 + "-" + sumError.get(id2));
    }
}
