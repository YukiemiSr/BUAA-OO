package com.oocourse.spec2.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JudgeOkay {
    private HashMap<Integer, HashMap<Integer, Integer>> map1;
    private HashMap<Integer, HashMap<Integer, Integer>> map2;
    private int id1;
    private int id2;
    private int value;

    public JudgeOkay(int id1, int id2, int value,
                     HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                     HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        this.map1 = beforeData;
        this.map2 = afterData;
        this.id1 = id1;
        this.id2 = id2;
        this.value = value;
    }

    public int forAns() {
        if (map1.size() != map2.size()) {
            return 1;
        }
        if (!line2()) {
            return 2;
        }
        if (!line3()) {
            return 3;
        }
        if (map1.containsKey(id1) && map1.containsKey(id2) && id1 != id2
            && map1.get(id1).containsKey(id2) && map1.get(id1).get(id2) + value > 0) {
            if (!line4()) {
                return 4;
            }
            if (!line5()) {
                return 5;
            }
            if (!line6()) {
                return 6;
            }
            if (!line7()) {
                return 7;
            }
            if (!line8()) {
                return 8;
            }
            if (!line9()) {
                return 9;
            }
            if (!line10()) {
                return 10;
            }
            if (!line11()) {
                return 11;
            }
            if (!line12()) {
                return 12;
            }
        } else {
            if (!line15()) {
                return 15;
            }
            if (!line16()) {
                return 16;
            }
            if (!line17()) {
                return 17;
            }
            if (!line20()) {
                return 20;
            }
            if (!line21()) {
                return 21;
            }
        }
        return 0;
    }

    boolean line2() {
        for (Integer innerKey : map1.keySet()) {
            if (!map2.containsKey(innerKey)) {
                return false;
            }
        }
        return true;
    }

    boolean line3() {
        for (Integer innerKey : map1.keySet()) {
            if (innerKey != id1 && innerKey != id2) {
                if (!areEqual(map1.get(innerKey), map2.get(innerKey))) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean line4() {
        return map2.get(id1).containsKey(id2) && map2.get(id2).containsKey(id1);
    }

    boolean line5() {
        int x = map1.get(id1).get(id2);
        return map2.get(id1).get(id2) == x + value;
    }

    boolean line6() {
        int x = map1.get(id2).get(id1);
        return map2.get(id2).get(id1) == x + value;
    }

    boolean line7() {
        return map1.get(id1).size() == map2.get(id1).size();
    }

    boolean line8() {
        return map1.get(id2).size() == map2.get(id2).size();
    }

    boolean line9() {
        return areKeysEqual(map1.get(id1), map2.get(id1));
    }

    boolean line10() {
        return areKeysEqual(map1.get(id2), map2.get(id2));
    }

    boolean line11() {
        HashMap<Integer, Integer> s1 = map1.get(id1);
        HashMap<Integer, Integer> s2 = map2.get(id1);
        for (Integer innerKey : s1.keySet()) {
            if (s2.get(innerKey) != s1.get(innerKey) && innerKey != id2) {
                return false;
            }
        }
        return true;
    }

    boolean line12() {
        HashMap<Integer, Integer> s1 = map1.get(id2);
        HashMap<Integer, Integer> s2 = map2.get(id2);
        for (Integer innerKey : s1.keySet()) {
            if (s2.get(innerKey) != s1.get(innerKey) && innerKey != id1) {
                return false;
            }
        }
        return true;
    }

    boolean line15() {
        return !map2.get(id1).containsKey(id2) && !map2.get(id2).containsKey(id1);
    }

    boolean line16() {
        return map1.get(id1).size() == map2.get(id1).size() + 1;
    }

    boolean line17() {
        return map1.get(id2).size() == map2.get(id2).size() + 1;
    }

    boolean line20() {
        HashMap<Integer, Integer> s1 = map1.get(id1);
        HashMap<Integer, Integer> s2 = map2.get(id1);
        for (Integer innerKey : s2.keySet()) {
            if (s2.get(innerKey) != s1.get(innerKey)) {
                return false;
            }
        }
        return true;
    }

    boolean line21() {
        HashMap<Integer, Integer> s1 = map1.get(id2);
        HashMap<Integer, Integer> s2 = map2.get(id2);
        for (Integer innerKey : s2.keySet()) {
            if (s2.get(innerKey) != s1.get(innerKey)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areEqual(HashMap<Integer, Integer> map1, HashMap<Integer, Integer> map2) {
        if (map1 == null || map2 == null) {
            return false;
        }
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Map.Entry<Integer, Integer> entry : map1.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if (!map2.containsKey(key) || !value.equals(map2.get(key))) {
                return false;
            }
        }
        return true;
    }

    public static boolean areKeysEqual(HashMap<Integer, Integer> map1,
                                       HashMap<Integer, Integer> map2) {
        if (map1 == null || map2 == null) {
            return false;
        }
        if (map1.size() != map2.size()) {
            return false;
        }
        for (Integer key : map1.keySet()) {
            if (!map2.containsKey(key)) {
                return false;
            }
        }
        return true;
    }

    public static boolean areValuesEqual(HashMap<Integer, Integer> map1,
                                         HashMap<Integer, Integer> map2) {
        if (map1 == null || map2 == null) {
            return false;
        }
        if (map1.size() != map2.size()) {
            return false;
        }
        List<Integer> values1 = new ArrayList<>(map1.values());
        List<Integer> values2 = new ArrayList<>(map2.values());
        Collections.sort(values1);
        Collections.sort(values2);
        return values1.equals(values2);
    }
}
