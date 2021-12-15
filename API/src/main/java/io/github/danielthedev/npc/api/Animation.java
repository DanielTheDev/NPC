package io.github.danielthedev.npc.api;

public enum Animation {

    SWING_MAIN_HAND(0),
    TAKE_DAMAGE(1),
    LEAVE_BED(2),
    SWING_OFFHAND(3),
    CRITICAL_EFFECT(4),
    MAGIC_CRITICAL_EFFECT(5);

    private final int type;

    Animation(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

}
