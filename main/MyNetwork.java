package com.oocourse.spec3.main;

import com.oocourse.spec3.exceptions.AcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.MyAcquaintanceNotFoundException;
import com.oocourse.spec3.exceptions.MyEmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.MyEqualEmojiIdException;
import com.oocourse.spec3.exceptions.MyEqualGroupIdException;
import com.oocourse.spec3.exceptions.MyEqualMessageIdException;
import com.oocourse.spec3.exceptions.MyEqualPersonIdException;
import com.oocourse.spec3.exceptions.MyEqualRelationException;
import com.oocourse.spec3.exceptions.MyGroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MyMessageIdNotFoundException;
import com.oocourse.spec3.exceptions.MyPersonIdNotFoundException;
import com.oocourse.spec3.exceptions.MyRelationNotFoundException;
import com.oocourse.spec3.exceptions.PathNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;

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
    private int sum = 0;
    private int cntTri = 0;
    private int cntFinalNode = 0;
    private HashMap<Integer, Integer> map;
    private HashMap<Integer, Integer> antimap;
    private HashMap<Integer, Integer> coldMap;
    private final HashSet<Pair<Integer, Integer>> backup;
    private final ArrayList<Integer> peopleIds;
    private HashMap<Integer, Integer> emojis;

    public MyNetwork() {
        this.peopleIds = new ArrayList<>();
        this.people = new HashMap<>();
        this.groups = new HashMap<>();
        this.antimap = new HashMap<>();
        this.messages = new HashMap<>();
        this.backup = new HashSet<>();
        this.emojis = new HashMap<>();
        this.coldMap = new HashMap<>();
        map = new HashMap<>();
        parent = new int[10010];
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
            antimap.put(sum, person.getId());
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
        if (find(x) != find(y)) {
            parent[find(x)] = find(y);
        }
    }

    public void remove(int id1, int id2) {
        backup.remove(new Pair<>(Math.min(id1, id2), Math.max(id1, id2)));
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
        return find(map.get(id1)) == find(map.get(id2));
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
    public void addMessage(Message message)
        throws EqualMessageIdException, EqualPersonIdException, MyEmojiIdNotFoundException {
        if (messages.containsKey(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if (message instanceof EmojiMessage) {
            int id = ((EmojiMessage) message).getEmojiId();
            if (!emojis.containsKey(id)) {
                throw new MyEmojiIdNotFoundException(id);
            }
            coldMap.put(message.getId(),id);
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
            if (getMessage(id) instanceof RedEnvelopeMessage) {
                int money = ((MyRedEnvelopeMessage) getMessage(id)).getMoney();
                getMessage(id).getPerson1().addMoney(-money);
                getMessage(id).getPerson2().addMoney(money);
            }
            if (getMessage(id) instanceof EmojiMessage) {
                int eid = ((EmojiMessage) getMessage(id)).getEmojiId();
                emojis.put(eid, emojis.get(eid) + 1);
            }
            messages.remove(id);
        } else if (getMessage(id).getType() == 1) {
            int flag1 = 0;
            int n;
            int money = 0;
            if (getMessage(id) instanceof RedEnvelopeMessage) {
                flag1 = 1;
                n = ((MyGroup) getMessage(id).getGroup()).getPeople().size();
                money = ((MyRedEnvelopeMessage) getMessage(id)).getMoney() / n;
                getMessage(id).getPerson1().addMoney(-(money * (n)));
            }
            if (getMessage(id) instanceof EmojiMessage) {
                int eid = ((EmojiMessage) getMessage(id)).getEmojiId();
                emojis.put(((EmojiMessage) getMessage(id)).getEmojiId(), emojis.get(eid) + 1);
            }
            for (Map.Entry entry : ((MyGroup) getMessage(id).getGroup()).getPeople().entrySet()) {
                ((MyPerson) entry.getValue()).addSocialValue(getMessage(id).getSocialValue());
                if (flag1 == 1) {
                    ((MyPerson) entry.getValue()).addMoney(money);
                }
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
    public boolean containsEmojiId(int id) {
        return emojis.containsKey(id);
    }

    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojis.put(id, 0);
    }

    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojis.get(id);
    }

    @Override
    public int deleteColdEmoji(int limit) {
        HashMap<Integer, Integer> e1 = new HashMap<>();
        for (Map.Entry entry : emojis.entrySet()) {
            if ((Integer) entry.getValue() >= limit) {
                e1.put((Integer) entry.getKey(), (Integer) entry.getValue());
            }
            else {
                messages.remove(coldMap.get((Integer) entry.getKey()));
            }
        }
        emojis = e1;
        return emojis.size();
    }

    @Override
    public int deleteColdEmojiOKTest(int limit, ArrayList<HashMap<Integer, Integer>> beforeData,
                                     ArrayList<HashMap<Integer, Integer>> afterData, int result) {
        OkTest okTest = new OkTest(limit,beforeData,afterData,result);
        return okTest.answer();
    }

    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        MyPerson person = (MyPerson) getPerson(personId);
        person.delNotice();
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
    public int queryLeastMoments(int id) throws PersonIdNotFoundException, PathNotFoundException {
        Qlm qlm1 = new Qlm();
        return qlm1.Query(people,id);
    }

}
