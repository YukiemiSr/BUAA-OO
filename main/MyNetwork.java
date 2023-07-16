package com.oocourse.spec1.main;

import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.MyEqualPersonIdException;
import com.oocourse.spec1.exceptions.MyEqualRelationException;
import com.oocourse.spec1.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec1.exceptions.MyRelationNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer,Integer> map;
    private int[] parent;
    private int sum;
    private int cntTri;
    private int cntFinalNode;

    public MyNetwork() {
        this.people = new HashMap<>();
        parent = new int[10010];
        map = new HashMap<>();
        sum = 0;
        cntTri = 0;
        cntFinalNode = 0;
    }

    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }

    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return people.get(id);
        }
        return null;
    }

    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        } else {
            people.put(person.getId(), person);
            parent[sum] = sum;
            map.put(person.getId(), sum);
            sum++;
            cntFinalNode++;
        }
    }

    @Override
    public void addRelation(int id1, int id2, int value)
        throws PersonIdNotFoundException, EqualRelationException {
        MyPerson p1 = (MyPerson) people.get(id1);
        MyPerson p2 = (MyPerson) people.get(id2);
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!people.containsKey(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            p1.addAcquaintance(p2.getId(),getPerson(p2.getId()));
            p1.addValue(p2.getId(), value);
            p2.addAcquaintance(p1.getId(),getPerson(p1.getId()));
            p2.addValue(p1.getId(), value);
            union(map.get(p1.getId()),map.get(p2.getId()));
            Integer[] keys = people.keySet().toArray(new Integer[0]);
            for (int i = 0; i < keys.length; i++) {
                if (people.get(keys[i]).isLinked(people.get(id1)) &&
                    people.get(keys[i]).isLinked(people.get(id2)) && keys[i] != id1
                    && keys[i] != id2) {
                    cntTri++;
                }
            }
        }

    }

    @Override
    public int queryValue(int id1, int id2)
        throws PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    public int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            cntFinalNode--;
            parent[rootX] = rootY;
        }
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        int x1 = map.get(id1);
        int x2 = map.get(id2);
        return find(x1) == find(x2);

    }

    @Override
    public int queryBlockSum() {
        return cntFinalNode;
    }

    @Override
    public int queryTripleSum() {
        return cntTri;
    }

    @Override
    public boolean queryTripleSumOKTest(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                        HashMap<Integer, HashMap<Integer, Integer>> afterData,
                                        int result) {
        return compareHashMaps(beforeData, afterData) && queryTripleSumOK(beforeData, result);
    }

    public static boolean compareHashMaps(HashMap<Integer, HashMap<Integer, Integer>> map1,
                                          HashMap<Integer, HashMap<Integer, Integer>> map2) {
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
            HashMap<Integer, Integer> innerMap1 = map1.get(key);
            HashMap<Integer, Integer> innerMap2 = map2.get(key);
            if (innerMap1.size() != innerMap2.size()) {
                return false;
            }
            for (Integer innerKey : innerMap1.keySet()) {
                if (!innerMap2.containsKey(innerKey)) {
                    return false;
                }
                if (!innerMap1.get(innerKey).equals(innerMap2.get(innerKey))) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean queryTripleSumOK(HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    int result) {
        int result1 = 0;
        for (Integer i : beforeData.keySet()) {
            for (Integer j : beforeData.keySet()) {
                if (i < j) {
                    for (Integer k : beforeData.keySet()) {
                        if (j < k) {
                            if (beforeData.get(i).containsKey(j) &&
                                beforeData.get(j).containsKey(k) &&
                                beforeData.get(k).containsKey(i)) {
                                result1++;
                            }
                        }
                    }
                }
            }
        }
        return result == result1;
    }
}
