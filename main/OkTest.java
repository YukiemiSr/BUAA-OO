package com.oocourse.spec3.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OkTest {
    private int limit;
    private ArrayList<HashMap<Integer, Integer>> beforeData;
    private ArrayList<HashMap<Integer, Integer>> afterData;
    private int result;

    public OkTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                  ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        this.limit = limit;
        this.beforeData = beforeData;
        this.afterData = afterData;
        this.result = result;
    }

    public int answer() {
        HashMap<Integer, Integer> oldemi = beforeData.get(0);
        HashMap<Integer, Integer> newemi = afterData.get(0);
        HashMap<Integer, Integer> newmessage = afterData.get(1);
        for (Map.Entry entry : oldemi.entrySet()) {
            if ((Integer) entry.getValue() >= limit) {
                if (!newemi.containsKey((Integer) entry.getKey())) {
                    return 1;
                }
            }
        }
        for (Map.Entry entry : newemi.entrySet()) {
            if (!(oldemi.containsKey((Integer) entry.getKey()) &&
                oldemi.get((Integer) entry.getKey()) == entry.getValue())) {
                return 2;
            }
        }
        int num = 0;
        int k = 0;
        for (Map.Entry entry : oldemi.entrySet()) {
            if ((Integer) entry.getValue() >= limit) {
                num++;
            } else {
                k++;
            }
        }
        if (num != newemi.size()) {
            return 3;
        }
        for (Map.Entry entry : beforeData.get(1).entrySet()) {
            if (entry.getValue() != null) { //正常信息
                int eid = (Integer) entry.getValue();
                if (newemi.containsKey(eid)) {
                    if (!newmessage.containsKey((Integer) entry.getKey())) {
                        return 5;
                    } else {
                        if (newmessage.get((Integer) entry.getKey()) != entry.getValue()) {
                            return 5;
                        }
                    }
                }
            } else {
                if (!newmessage.containsKey((Integer) entry.getKey())) {
                    return 6;
                } else {
                    if (newmessage.get((Integer) entry.getKey()) != null) {
                        return 6;
                    }
                }
            }
        }
        if (beforeData.get(1).size() != newmessage.size() + k) {
            return 7;
        }
        if (result != newemi.size()) {
            return 8;
        }
        return 0;
    }
}
