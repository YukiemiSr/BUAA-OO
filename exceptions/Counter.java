package com.oocourse.spec1.exceptions;

public class Counter {
    private int count;

    public Counter() {
        count = 0;
    }

    public void increase() {
        count += 1;
    }

    public int getCounter() {
        return count;
    }
}
