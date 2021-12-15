package io.github.danielthedev.npc.api;

import java.util.ArrayList;
import java.util.List;

public enum EntityState {

    DEFAULT(0x00),
    ON_FIRE(0x01),
    @Deprecated CROUCHING(0x02),
    @Deprecated UNUSED(0x04),
    SPRINTING(0x08),
    SWIMMING(0x10),
    INVISIBLE(0x20),
    GLOWING(0x40),
    FLYING(0x80),
    ALL(0xFF);

    private final int mask;

    EntityState(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    public static int createMask(EntityState... entityStates) {
        int mask = 0;
        for(EntityState entityState : entityStates) {
            mask |= entityState.mask;
        }
        return mask;
    }

    public static EntityState[] fromMask(int mask) {
        List<EntityState> list = new ArrayList<>();
        for(EntityState entityState : values()) {
            if((entityState.mask & mask) == entityState.mask) {
                list.add(entityState);
            }
        }
        return list.toArray(new EntityState[list.size()]);
    }
}
