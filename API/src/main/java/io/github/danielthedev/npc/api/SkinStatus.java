package io.github.danielthedev.npc.api;

import java.util.ArrayList;
import java.util.List;

public enum SkinStatus {

    CAPE_ENABLED(0x01),
    JACKET_ENABLED(0x02),
    LEFT_SLEEVE_ENABLED(0x04),
    RIGHT_SLEEVE_ENABLED(0x08),
    LEFT_PANTS_LEG_ENABLED(0x10),
    RIGHT_PANTS_LEG_ENABLED(0x20),
    HAT_ENABLED(0x40),
    @Deprecated UNUSED(0x80),
    ALL_ENABLED(0xFF);

    private final int mask;

    SkinStatus(int mask) {
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    public static int createMask(SkinStatus... skinStatuses) {
        int mask = 0;
        for(SkinStatus handStatus : skinStatuses) {
            mask |= handStatus.mask;
        }
        return mask;
    }

    public static SkinStatus[] fromMask(int mask) {
        List<SkinStatus> list = new ArrayList<>();
        for(SkinStatus skinStatus : values()) {
            if((skinStatus.mask & mask) == skinStatus.mask) {
                list.add(skinStatus);
            }
        }
        return list.toArray(new SkinStatus[list.size()]);
    }
}
