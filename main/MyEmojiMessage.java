package com.oocourse.spec3.main;

public class MyEmojiMessage extends MyMessage implements EmojiMessage {
    private final int emojiId;

    public MyEmojiMessage(int messageId, int emojiId, Person messagePerson1,
                          Person messagePerson2) {
        super(messageId, emojiId, messagePerson1, messagePerson2);
        this.emojiId = emojiId;
    }

    public MyEmojiMessage(int messageId, int emojiId, Person messagePerson1,
                          Group messageGroup) {
        super(messageId, emojiId, messagePerson1, messageGroup);
        this.emojiId = emojiId;
    }

    @Override
    public int getEmojiId() {
        return this.emojiId;
    }
}
