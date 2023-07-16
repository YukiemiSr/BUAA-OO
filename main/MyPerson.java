package com.oocourse.spec2.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPerson implements Person {

    private final int id;
    private final String name;
    private final int age;
    private HashMap<Integer, Person> acquaintance;
    private HashMap<Integer, Integer> value;
    private int socialValue;
    private ArrayList<Message> messages;
    private int bestId;
    private int bestValue;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.socialValue = 0;
        this.value = new HashMap<>();
        this.acquaintance = new HashMap<>();
        this.messages = new ArrayList<>();
        this.bestId = -1;
        this.bestValue = -1;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getAge() {
        return this.age;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Person) obj).getId() == this.id;
    }

    @Override
    public boolean isLinked(Person person) {
        return acquaintance.containsKey(person.getId()) || person.getId() == id;
    }

    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person.getId())) {
            return value.get(person.getId());
        }
        return 0;
    }

    @Override
    public void addSocialValue(int num) {
        this.socialValue += num;
    }

    @Override
    public int getSocialValue() {
        return this.socialValue;
    }

    @Override
    public List<Message> getMessages() {
        return this.messages;
    }

    @Override
    public List<Message> getReceivedMessages() {
        ArrayList list = new ArrayList<>();
        if (messages.size() < 5) {
            for (int i = 0; i < messages.size(); i++) {
                list.add(messages.get(i));
            }
        } else {
            for (int i = 0; i < 5; i++) {
                list.add(messages.get(i));
            }
        }
        return list;
    }

    @Override
    public int compareTo(Person o) {
        return name.compareTo(o.getName());
    }

    public void addAcquaintance(int id, Person person, int value) {
        if (!acquaintance.containsKey(id)) {
            acquaintance.put(id, person);
            this.value.put(id, value);
        }
    }

    public HashMap<Integer, Person> getAcquaintance() {
        return acquaintance;
    }

    public void removeAcquaintance(Person person) {
        acquaintance.remove(person.getId());
        value.remove(person);
    }

    public int getBestId() {
        return bestId;
    }

    public void addMessage(Message message) {
        ArrayList<Message> messages1 = new ArrayList<>();
        messages1.add(message);
        for (Message message2 : messages) {
            messages1.add(message2);
        }
        messages = messages1;
    }

    public void modifyBestId() {
        bestValue = -1;
        bestId = -1;
        if (acquaintance.isEmpty()) {
            return;
        }
        for (Map.Entry entry : acquaintance.entrySet()) {
            MyPerson person = (MyPerson) entry.getValue();
            int id = person.getId();
            int value = queryValue(person);
            if (value >= bestValue) {
                if (value == bestValue) {
                    if (id < bestId) {
                        bestId = id;
                    }
                } else {
                    bestValue = value;
                    bestId = id;
                }
            }
        }
    }
}
