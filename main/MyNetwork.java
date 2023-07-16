package com.oocourse.spec2.main;

import com.oocourse.spec2.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.MyAcquaintanceNotFoundException;
import com.oocourse.spec2.exceptions.MyEqualGroupIdException;
import com.oocourse.spec2.exceptions.MyEqualMessageIdException;
import com.oocourse.spec2.exceptions.MyEqualPersonIdException;
import com.oocourse.spec2.exceptions.MyEqualRelationException;
import com.oocourse.spec2.exceptions.MyGroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MyMessageIdNotFoundException;
import com.oocourse.spec2.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec2.exceptions.MyRelationNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyNetwork implements Network {
    private HashMap<Integer, Person> people;
    private HashMap<Integer, Group> groups;
    private HashMap<Integer, Message> messages;
    private int[] parent;
    private int sum;
    private int cntTri;
    private int cntFinalNode;
    private HashMap<Integer, Integer> map;
    private final HashSet<Pair<Integer, Integer>> backup;
    private final ArrayList<Integer> peopleIds;

    public MyNetwork() {
        this.peopleIds = new ArrayList<>();
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
        this.messages = new HashMap<>();
        this.backup = new HashSet<>();
        map = new HashMap<>();
        parent = new int[10010];
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
            peopleIds.add(person.getId());
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
            p1.addAcquaintance(p2.getId(), getPerson(p2.getId()), value);
            p2.addAcquaintance(p1.getId(), getPerson(p1.getId()), value);
            p1.modifyBestId();
            p2.modifyBestId();
            union(map.get(p1.getId()), map.get(p2.getId()));
            int low = Math.min(id1, id2);
            int high = Math.max(id1, id2);
            backup.add(new Pair<>(low, high));
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
    public void modifyRelation(int id1, int id2, int value)
        throws PersonIdNotFoundException, EqualPersonIdException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (id1 == id2) {
            throw new MyEqualPersonIdException(id1);
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        if ((getPerson(id1)).queryValue(getPerson(id2)) + value > 0) {
            int x = getPerson(id1).queryValue(getPerson(id2)) + value;
            ((MyPerson) getPerson(id1)).removeAcquaintance(getPerson(id2));
            ((MyPerson) getPerson(id1)).addAcquaintance(id2, getPerson(id2), x);
            ((MyPerson) getPerson(id2)).removeAcquaintance(getPerson(id1));
            ((MyPerson) getPerson(id2)).addAcquaintance(id1, getPerson(id1), x);
            ((MyPerson) getPerson(id1)).modifyBestId();
            ((MyPerson) getPerson(id2)).modifyBestId();
        } else {
            for (Map.Entry entry : ((MyPerson) getPerson(id1)).getAcquaintance().entrySet()) {
                if ((Integer) entry.getKey() != id2) {
                    if (((MyPerson) entry.getValue()).isLinked(getPerson(id1)) &&
                        ((MyPerson) entry.getValue()).isLinked(getPerson(id2))) {
                        cntTri--;
                    }
                }
            }
            ((MyPerson) getPerson(id1)).removeAcquaintance(getPerson(id2));
            ((MyPerson) getPerson(id2)).removeAcquaintance(getPerson(id1));
            ((MyPerson) getPerson(id1)).modifyBestId();
            ((MyPerson) getPerson(id2)).modifyBestId();
            remove(id1, id2);
            if (!isCircle(id1, id2)) {
                cntFinalNode++;
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
            parent[rootX] = rootY;
            cntFinalNode--;
        }
    }

    public void union1(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootX] = rootY;
        }
    }

    public void remove(int id1, int id2) {
        int low = Math.min(id1, id2);
        int high = Math.max(id1, id2);
        backup.remove(new Pair<>(low, high));
        for (Integer i : peopleIds) {
            parent[map.get(i)] = map.get(i);
        }
        for (Pair pair : backup) {
            union1(map.get((Integer) pair.getKey()), map.get((Integer) pair.getValue()));
        }
    }

    @Override
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
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    @Override
    public Group getGroup(int id) {
        return groups.get(id);
    }

    @Override
    public void addToGroup(int id1, int id2)
        throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (getGroup(id2).getSize() <= 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getValueSum();
    }

    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    @Override
    public void delFromGroup(int id1, int id2)
        throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!groups.containsKey(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!people.containsKey(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
    }

    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message.getType() == 0) {
            if (message.getPerson1().equals(message.getPerson2())) {
                throw new MyEqualPersonIdException(message.getPerson1().getId());
            }
        }
        messages.put(message.getId(), message);
    }

    @Override
    public Message getMessage(int id) {
        return messages.get(id);
    }

    @Override
    public void sendMessage(int id)
        throws RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        if (!messages.containsKey(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        if (getMessage(id).getType() == 0) {
            if (!(getMessage(id).getPerson1().isLinked(getMessage(id).getPerson2()))) {
                throw new MyRelationNotFoundException(getMessage(id).getPerson1().getId(),
                    getMessage(id).getPerson2().getId());
            }
        }
        if (getMessage(id).getType() == 1) {
            if (!(getMessage(id).getGroup().hasPerson(getMessage(id).getPerson1()))) {
                throw new MyPersonIdNotFoundException(getMessage(id).getPerson1().getId());
            }
        }
        if (getMessage(id).getType() == 0) {
            getMessage(id).getPerson1().addSocialValue(getMessage(id).getSocialValue());
            getMessage(id).getPerson2().addSocialValue(getMessage(id).getSocialValue());
            ((MyPerson) getMessage(id).getPerson2()).addMessage(getMessage(id));
            messages.remove(id);
        } else if (getMessage(id).getType() == 1) {
            for (Map.Entry entry : ((MyGroup) getMessage(id).getGroup()).getPeople().entrySet()) {
                ((MyPerson) entry.getValue()).addSocialValue(getMessage(id).getSocialValue());
            }
            messages.remove(id);
        }
    }

    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }

    @Override
    public int queryBestAcquaintance(int id)
        throws PersonIdNotFoundException, AcquaintanceNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        if (((MyPerson) getPerson(id)).getAcquaintance().size() == 0) {
            throw new MyAcquaintanceNotFoundException(id);
        }
        return ((MyPerson) getPerson(id)).getBestId();
    }

    @Override
    public int queryCoupleSum() {
        Integer[] keys = people.keySet().toArray(new Integer[0]);
        int sum = 0;
        for (Integer key : keys) {
            if (((MyPerson) people.get(key)).getAcquaintance().size() > 0) {
                int x = ((MyPerson) people.get(key)).getBestId();
                if (((MyPerson) people.get(x)).getBestId() == key) {
                    sum++;
                }
            }
        }

        return sum / 2;
    }

    @Override
    public int modifyRelationOKTest(int id1, int id2, int value,
                                    HashMap<Integer, HashMap<Integer, Integer>> beforeData,
                                    HashMap<Integer, HashMap<Integer, Integer>> afterData) {
        JudgeOkay judge = new JudgeOkay(id1, id2, value, beforeData, afterData);
        if (!beforeData.containsKey(id1) || !beforeData.containsKey(id2)
            || id1 == id2 || !beforeData.get(id1).containsKey(id2) ||
            !beforeData.get(id2).containsKey(id1)) {
            if (compareHashMaps(beforeData, afterData)) {
                return 0;
            } else {
                return -1;
            }
        }
        return judge.forAns();
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
}
