package io.github.danielthedev.npc.api;

public enum Hand {

    LEFT(0),
    RIGHT(1);

    private final int type;

    Hand(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static Hand fromByte(byte type) {
        for(Hand hand : values()) {
            if(type == hand.type) {
                return hand;
            }
        }
        return null;
    }
}
