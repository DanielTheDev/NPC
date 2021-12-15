package io.github.danielthedev.npc;

import io.github.danielthedev.npc.api.NPC;
import org.bukkit.Location;

import java.util.UUID;

public class NPCManager {

    public static NPC createNPC(Location location, String displayName) {
        return createNPC(UUID.randomUUID(), location, displayName);
    }

    public static NPC createNPC(UUID uuid, Location location, String displayName) {
        return switch (NMSVersion.getNMSVersion()) {
            case v1_16_R1 -> null;
            case v1_16_R2 -> null;
            case v1_16_R3 -> null;
            case v1_17_R1 -> new io.github.danielthedev.npc.nms.v1_17_R1.CraftNPC(uuid, location, displayName);
            case v1_18_R1 -> new io.github.danielthedev.npc.nms.v1_18_R1.CraftNPC(uuid, location, displayName);
        };
    }

}
