package com.oocourse.spec2.main;

public class MainClass {
    public static void main(String[] args) throws Exception {
        Runner runner = new Runner(MyPerson.class, MyNetwork.class, MyGroup.class,
            MyMessage.class);
        runner.run();
    }
}
