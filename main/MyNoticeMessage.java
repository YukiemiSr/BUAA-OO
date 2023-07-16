package com.oocourse.spec3.main;

public class MyNoticeMessage extends MyMessage implements NoticeMessage {

    private String string;

    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Person messagePerson2) {
        super(messageId, noticeString.length(), messagePerson1, messagePerson2);
        this.string = noticeString;
    }

    public MyNoticeMessage(int messageId, String noticeString, Person messagePerson1,
                           Group messageGroup) {
        super(messageId, noticeString.length(), messagePerson1, messageGroup);
    }

    @Override
    public String getString() {
        return string;
    }
}
