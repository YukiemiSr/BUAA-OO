package com.oocourse.spec2.main;

import java.util.HashMap;

public class MyGroup implements Group {

    private int id;
    private HashMap<Integer, Person> people;

    public MyGroup(int id) {
        this.id = id;
        this.people = new HashMap<>();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Group) {
            return ((Group) obj).getId() == id;
        }
        return false;
    }

    @Override
    public void addPerson(Person person) {
        people.put(person.getId(), person);
    }

    @Override
    public boolean hasPerson(Person person) {
        return people.containsKey(person.getId());
    }

    @Override
    public int getValueSum() {
        int sum = 0;
        Integer[] keys = people.keySet().toArray(new Integer[0]);
        for (int i = 0; i < keys.length; i++) {
            for (int j = 0; j < keys.length; j++) {
                if (people.get(keys[i]).isLinked(people.get(keys[j]))) {
                    sum += people.get(keys[i]).queryValue(people.get(keys[j]));
                }
            }
        }
        return sum;
    }

    @Override
    public int getAgeMean() {
        int sum = 0;
        Integer[] keys = people.keySet().toArray(new Integer[0]);
        if (keys.length == 0) {
            return 0;
        }
        for (Integer key : keys) {
            sum += people.get(key).getAge();
        }
        return sum / keys.length;
    }

    @Override
    public int getAgeVar() {
        int sum = 0;
        Integer[] keys = people.keySet().toArray(new Integer[0]);
        if (keys.length == 0) {
            return 0;
        }
        for (Integer key : keys) {
            sum += (people.get(key).getAge() - getAgeMean()) *
                (people.get(key).getAge() - getAgeMean());
        }
        return sum / keys.length;
    }

    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person.getId(), person);
        }
    }

    @Override
    public int getSize() {
        return people.size();
    }

    public HashMap<Integer, Person> getPeople() {
        return people;
    }
}
