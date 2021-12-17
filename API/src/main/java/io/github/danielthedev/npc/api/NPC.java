package io.github.danielthedev.npc.api;

import com.mojang.authlib.GameProfile;
import io.github.danielthedev.npc.api.bukkit.SkinTextures;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface NPC {

    void spawnNPC(Player receiver);

    void spawnNPC(Collection<Player> receivers);

    void destroyNPC(Collection<Player> receivers);

    void destroyNPC(Player receiver);

    void reloadNPC(Collection<Player> receivers);

    void reloadNPC(Player receiver);

    void teleportNPC(Collection<Player> receivers, Location location, boolean onGround);

    void teleportNPC(Player receiver, Location location, boolean onGround);

    void updateMetadata(Collection<Player> receivers);

    void updateMetadata(Player receiver);

    void updateGameMode(Collection<Player> receivers);

    void updateGameMode(Player receiver);

    void updatePing(Collection<Player> receivers);

    void updatePing(Player receiver);

    void updateTabListName(Collection<Player> receivers);

    void updateTabListName(Player receiver);

    void removeFromTabList(Collection<Player> receivers);

    void removeFromTabList(Player receiver);

    void addToTabList(Collection<Player> receivers);

    void addToTabList(Player receiver);

    void playAnimation(Collection<Player> receivers, Animation animation);

    void playAnimation(Player receiver, Animation animation);

    void lookAtPlayer(Collection<Player> receivers, Player target);

    void lookAtPlayer(Player receiver, Player target);

    void lookAtPoint(Collection<Player> receivers, Location location);

    void lookAtPoint(Player receiver, Location location);

    void rotateHead(Collection<Player> receivers, float pitch, float yaw);

    void rotateHead(Player receiver, float pitch, float yaw);

    void setTabListName(String name);

    void setEquipment(Collection<Player> receivers, ItemSlot slot, org.bukkit.inventory.ItemStack itemStack);

    void setEquipment(Player receiver, ItemSlot slot, org.bukkit.inventory.ItemStack itemStack);

    void setPassenger(Collection<Player> receivers, int... entityIDs);

    void setPassenger(Player receiver, int... entityIDs);

    void setNameTagVisibility(Collection<Player> receivers, boolean show);

    void setNameTagVisibility(Player receiver, boolean show);

    void setParrotLeftShoulder(Consumer<Parrot> callback, World world);

    void setParrotRightShoulder(Consumer<Parrot> callback, World world);

    void setMetaData(MetaData metaData);

    void setSkin(SkinTextures skinTextures);

    void setASyncSkinByUsername(Plugin plugin, Collection<Player> receivers, String username);

    void setASyncSkinByUsername(Plugin plugin, Player receiver, String username);

    void setASyncSkinByUUID(Plugin plugin, Collection<Player> receivers, UUID uuid);

    void setASyncSkinByUUID(Plugin plugin, Player receiver, UUID uuid);

    void setASyncSkinByUsername(Plugin plugin, Player receiver, String username, BiConsumer<Boolean, NPC> callback);

    void setASyncSkinByUsername(Plugin plugin, Collection<Player> receivers, String username, BiConsumer<Boolean, NPC> callback);

    void setASyncSkinByUUID(Plugin plugin, Player receiver, UUID uuid, BiConsumer<Boolean, NPC> callback);
    
    void setASyncSkinByUUID(Plugin plugin, Collection<Player> receivers, UUID uuid, BiConsumer<Boolean, NPC> callback);

    void setPing(Ping ping);

    void setGameMode(Gamemode gamemode);

    void setDisplayName(String displayName);
    
    int getEntityID();

    GameProfile getProfile();

    Location getLocation();

    Location getEyeLocation();

    Ping getPing();

    Gamemode getGameMode();

    String getDisplayName();

    MetaData getMetaData();

    NMSVersion getNMSVersion();
}
