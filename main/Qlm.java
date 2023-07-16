package com.oocourse.spec3.main;

import com.oocourse.spec3.exceptions.MyPathNotFoundException;
import com.oocourse.spec3.exceptions.MyPersonIdNotFoundException;
import javafx.util.Pair;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

public class Qlm {
    public int Query(HashMap<Integer,Person> people, int id)
        throws MyPersonIdNotFoundException, MyPathNotFoundException {
        MyPerson origin = (MyPerson) people.get(id);
        if (origin == null) { throw new MyPersonIdNotFoundException(id); }
        HashSet<Integer> visited = new HashSet<>();
        HashMap<Integer, Integer> dis = new HashMap<>();
        HashMap<Integer, Integer> tree = new HashMap<>(); //构建最短路路径森林
        for (int id1 : people.keySet()) {
            tree.put(id1, id1);
            dis.put(id1, 0x3f3f3f3f);
        }
        PriorityQueue<Pair<Integer, Integer>> queue = new PriorityQueue<>(
            Comparator.comparingInt(Pair::getKey));
        dis.put(id, 0);
        queue.add(new Pair<>(0, id));
        while (queue.size() != 0) {
            int x = queue.poll().getValue();
            if (visited.contains(x)) { continue; }
            visited.add(x);
            MyPerson myPerson = (MyPerson) people.get(x);
            for (Map.Entry<Integer, Person> personEntry : myPerson.getAcquaintance().entrySet()) {
                Integer oldDisY = dis.get(personEntry.getKey());
                Integer oldDisX = dis.get(x);
                if (oldDisY > oldDisX + myPerson.queryValue(personEntry.getValue())) {
                    if (x != id) { tree.put(personEntry.getKey(), x); } //来自同一颗最短路径树，并查集维护
                    oldDisY = oldDisX + myPerson.queryValue(personEntry.getValue());
                    dis.put(personEntry.getKey(), oldDisY);
                    queue.add(new Pair<>(oldDisY, personEntry.getKey()));
                }
            }
        }
        int ans = 0x3f3f3f3f;
        for (Map.Entry<Integer, Person> personEntry : origin.getAcquaintance().entrySet()) {
            if (!personEntry.getKey().equals(tree.get(personEntry.getKey()))) {
                ans = Math.min(ans,
                    dis.get(personEntry.getKey()) + origin.queryValue(personEntry.getValue()));
            }
        }
        for (Map.Entry<Integer, Person> personEntry : people.entrySet()) {
            MyPerson myPerson1 = (MyPerson) personEntry.getValue();
            for (Map.Entry<Integer, Person> personEntry1 : myPerson1.getAcquaintance().entrySet()) {
                if (myPerson1.getId() != id && personEntry1.getKey() != id
                    && findRoot(myPerson1.getId(), tree) !=
                    findRoot(personEntry1.getKey(), tree)) { //两个点不能在一颗树上，否则无法形成环，这里顺带路径压缩降低时间复杂度
                    ans = Math.min(ans,
                        dis.get(myPerson1.getId()) + dis.get(personEntry1.getKey())
                            + myPerson1.queryValue(personEntry1.getValue()));
                }
            }
        }
        if (ans == 0x3f3f3f3f) { throw new MyPathNotFoundException(id); }
        return ans;
    }

    int findRoot(int id, HashMap<Integer,Integer> tree) {
        if (tree.get(id) != id) {
            tree.put(id,findRoot(tree.get(id), tree));
        }
        return tree.get(id);
    }

}
