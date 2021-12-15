package io.github.danielthedev.npc.api;

import java.util.ArrayList;
import java.util.List;

public enum HandStatus {

    MAIN_HAND(0x00),
    HAND_ACTIVE(0x01),
    OFF_HAND(0x02),
    RIPTIDE_SPIN_ATTACK(0x04),
    ALL(0x07);

    private final int mask;

    HandStatus(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    public static int createMask(HandStatus... handStatuses) {
        int mask = 0;
        for(HandStatus handStatus : handStatuses) {
            mask |= handStatus.mask;
        }
        return mask;
    }

    public static HandStatus[] fromMask(int mask) {
        List<HandStatus> list = new ArrayList<>();
        for(HandStatus handStatus : values()) {
            if((handStatus.mask & mask) == handStatus.mask) {
                list.add(handStatus);
            }
        }
        return list.toArray(new HandStatus[list.size()]);
    }
}
