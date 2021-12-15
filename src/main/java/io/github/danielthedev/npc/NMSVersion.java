package io.github.danielthedev.npc;

import org.bukkit.Bukkit;

public enum NMSVersion {

    v1_16_R1,
    v1_16_R2,
    v1_16_R3,
    v1_17_R1,
    v1_18_R1;

    private static final NMSVersion VERSION;

    static {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        String version = packageName.substring(packageName.lastIndexOf('.') + 1);
        VERSION = NMSVersion.valueOf(version);
    }

    public static NMSVersion getNMSVersion() {
        return VERSION;
    }
}
