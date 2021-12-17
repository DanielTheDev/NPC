package io.github.danielthedev.npc;

import io.github.danielthedev.npc.api.NMSVersion;
import io.github.danielthedev.npc.api.NPC;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

public class NPCFactory {

    private static final NMSVersion VERSION;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        VERSION = NMSVersion.valueOf(version);
    }

    public static NPC createNPC(Location location, String displayName) {
        return createNPC(UUID.randomUUID(), location, displayName);
    }

    public static NPC createNPC(UUID uuid, Location location, String displayName) {
        return switch (getNMSVersion()) {
            case v1_16_R1 -> null;
            case v1_16_R2 -> null;
            case v1_16_R3 -> null;
            case v1_17_R1 -> new io.github.danielthedev.npc.nms.v1_17_R1.CraftNPC(uuid, location, displayName);
            case v1_18_R1 -> new io.github.danielthedev.npc.nms.v1_18_R1.CraftNPC(uuid, location, displayName);
        };
    }

    public static NMSVersion getNMSVersion() {
        return VERSION;
    }
}
