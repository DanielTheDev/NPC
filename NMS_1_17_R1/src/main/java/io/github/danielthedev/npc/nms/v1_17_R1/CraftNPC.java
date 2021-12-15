package io.github.danielthedev.npc.nms.v1_17_R1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.datafixers.util.Pair;
import io.github.danielthedev.npc.api.*;
import io.github.danielthedev.npc.api.bukkit.CraftBukkitObject;
import io.github.danielthedev.npc.api.bukkit.SkinTextures;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketDataSerializer;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.entity.animal.EntityParrot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.ScoreboardTeam;
import net.minecraft.world.scores.ScoreboardTeamBase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftParrot;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_17_R1.util.CraftChatMessage;
import org.bukkit.entity.Parrot;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CraftNPC implements NPC {

    private static AtomicInteger atomicInteger;

    private final String hideTeam;
    private final int entityID; //unique entityID the server holds to find/modify existing entities. Be careful when assigning values that they do not overlap
    private final Location location;

    private GameProfile profile;
    private MetaData metadata = new CraftMetaData();
    private Ping ping = Ping.FIVE_BARS;
    private Gamemode gamemode = Gamemode.CREATIVE;
    private String displayName;

    static {
        try {
            Field field = Entity.class.getDeclaredField("b");
            field.setAccessible(true);
            atomicInteger = (AtomicInteger) field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public CraftNPC(UUID uuid, Location location, String displayName) {
        this.entityID = atomicInteger.incrementAndGet();

        this.profile = new GameProfile(uuid, displayName);
        this.location = location;
        this.displayName = displayName;
        this.hideTeam = "hide-" + Integer.toHexString(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));
    }

    public CraftNPC(Location location, String displayName) {
        this(UUID.randomUUID(), location, displayName);
    }

    public void spawnNPC(Collection<Player> players) {
        players.forEach(this::spawnNPC);
    }

    public void spawnNPC(Player player) {
        this.addToTabList(player);
        this.sendPacket(player, this.getEntitySpawnPacket());
        this.updateMetadata(player);
    }

    public void destroyNPC(Collection<Player> players) {
        players.forEach(this::destroyNPC);
    }

    public void destroyNPC(Player player) {
        this.sendPacket(player, this.getPlayerInfoPacket(PlayerInfo.REMOVE_PLAYER));
        this.sendPacket(player, this.getEntityDestroyPacket());
    }

    public void reloadNPC(Collection<Player> players) {
        players.forEach(this::reloadNPC);
    }

    public void reloadNPC(Player player) {
        this.destroyNPC(player);
        this.spawnNPC(player);
    }

    public void teleportNPC(Collection<Player> players, Location location, boolean onGround) {
        players.forEach(p->this.teleportNPC(p, location, onGround));
    }

    public void teleportNPC(Player player, Location location, boolean onGround) {
        this.location.setX(location.getX());
        this.location.setY(location.getY());
        this.location.setZ(location.getZ());
        this.location.setPitch(location.getPitch());
        this.location.setYaw(location.getYaw());
        this.sendPacket(player, this.getEntityTeleportPacket(onGround));
        this.rotateHead(player, location.getPitch(), location.getYaw());
    }

    public void updateMetadata(Collection<Player> players) {
        players.forEach(this::updateMetadata);
    }

    public void updateMetadata(Player player) {
        this.sendPacket(player, this.getEntityMetadataPacket());
    }

    public void updateGameMode(Collection<Player> players) {
        players.forEach(this::updateGameMode);
    }

    public void updateGameMode(Player player) {
        this.sendPacket(player, this.getPlayerInfoPacket(PlayerInfo.UPDATE_GAME_MODE));
    }

    public void updatePing(Collection<Player> players) {
        players.forEach(this::updatePing);
    }

    public void updatePing(Player player) {
        this.sendPacket(player, this.getPlayerInfoPacket(PlayerInfo.UPDATE_LATENCY));
    }

    public void updateTabListName(Collection<Player> players) {
        players.forEach(this::updateTabListName);
    }

    public void updateTabListName(Player player) {
        this.sendPacket(player, this.getPlayerInfoPacket(PlayerInfo.UPDATE_DISPLAY_NAME));
    }

    public void removeFromTabList(Collection<Player> players) {
        players.forEach(this::removeFromTabList);
    }

    public void removeFromTabList(Player player) {
        this.sendPacket(player, this.getPlayerInfoPacket(PlayerInfo.REMOVE_PLAYER));
    }

    public void addToTabList(Collection<Player> players) {
        players.forEach(this::addToTabList);
    }

    public void addToTabList(Player player) {
        this.sendPacket(player, this.getPlayerInfoPacket(PlayerInfo.ADD_PLAYER));
    }

    public void playAnimation(Collection<Player> players, Animation animation) {
        players.forEach(p->this.playAnimation(p, animation));
    }

    public void playAnimation(Player player, Animation animation) {
        this.sendPacket(player, this.getEntityAnimationPacket(animation));
    }

    public void lookAtPlayer(Collection<Player> players, Player target) {
        players.forEach(p->this.lookAtPlayer(p, target));
    }

    public void lookAtPlayer(Player player, Player target) {
        this.lookAtPoint(player, target.getEyeLocation());
    }

    public void lookAtPoint(Collection<Player> players, Location location) {
        players.forEach(p->this.lookAtPoint(p, location));
    }

    public void lookAtPoint(Player player, Location location) {
        Location eyeLocation = this.getEyeLocation();
        float yaw = (float) Math.toDegrees(Math.atan2(location.getZ() - eyeLocation.getZ(), location.getX()-eyeLocation.getX())) - 90;
        yaw = (float) (yaw + Math.ceil( -yaw / 360 ) * 360);

        float deltaXZ = (float) Math.sqrt(Math.pow(eyeLocation.getX()-location.getX(), 2) + Math.pow(eyeLocation.getZ()-location.getZ(), 2));
        float pitch = (float) Math.toDegrees(Math.atan2(deltaXZ, location.getY()-eyeLocation.getY())) - 90;

        pitch = (float) (pitch + Math.ceil( -pitch / 360 ) * 360);

        this.rotateHead(player, pitch, yaw);
    }

    public void rotateHead(Collection<Player> players, float pitch, float yaw) {
        players.forEach(p->this.rotateHead(p, pitch, yaw));
    }

    public void rotateHead(Player player, float pitch, float yaw) {
        this.location.setPitch(pitch);
        this.location.setYaw(yaw);
        this.sendPacket(player, this.getEntityLookPacket());
        this.sendPacket(player, this.getEntityHeadRotatePacket());
    }

    public void setTabListName(String name) {
        this.displayName = name;
    }

    public void setEquipment(Collection<Player> players, ItemSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        players.forEach(p->this.setEquipment(p, slot, itemStack));
    }

    public void setEquipment(Player player, ItemSlot slot, org.bukkit.inventory.ItemStack itemStack) {
        this.sendPacket(player, this.getEntityEquipmentPacket(NMSConverter.convertToNMS(slot), CraftItemStack.asNMSCopy(itemStack)));
    }

    public void setPassenger(Collection<Player> players, int... entityIDs) {
        players.forEach(p->this.setPassenger(p, entityIDs));
    }

    public void setPassenger(Player player, int... entityIDs) {
        this.sendPacket(player, getEntityAttachPacket(entityIDs));
    }

    private void sendPacket(Player player, Packet<?> packet) {
        ((CraftPlayer)(player)).getHandle().b.sendPacket(packet);
    }

    public void setNameTagVisibility(Collection<Player> players, boolean show) {
        players.forEach(p->this.setNameTagVisibility(p, show));
    }

    public void setNameTagVisibility(Player player, boolean show) {
        ScoreboardTeam team = new ScoreboardTeam(new Scoreboard(), this.hideTeam);
        if(show) {
            PacketPlayOutScoreboardTeam leavePacket = PacketPlayOutScoreboardTeam.a(team, this.profile.getName(), PacketPlayOutScoreboardTeam.a.b);
            this.sendPacket(player, leavePacket);
        } else {
            team.setNameTagVisibility(ScoreboardTeamBase.EnumNameTagVisibility.b);
            PacketPlayOutScoreboardTeam createPacket = PacketPlayOutScoreboardTeam.a(team, true);
            PacketPlayOutScoreboardTeam joinPacket = PacketPlayOutScoreboardTeam.a(team, this.profile.getName(), PacketPlayOutScoreboardTeam.a.a);
            this.sendPacket(player, createPacket);
            this.sendPacket(player, joinPacket);
        }
    }

    public NBTTagCompound createParrot(Consumer<Parrot> callback, World world) {
        EntityParrot entityParrot = new EntityParrot(EntityTypes.al, ((CraftWorld)world).getHandle());
        CraftParrot parrot = new CraftParrot((CraftServer) Bukkit.getServer(), entityParrot);
        callback.accept(parrot);
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        entityParrot.d(nbtTagCompound);
        return nbtTagCompound;
    }

    public void setParrotLeftShoulder(Consumer<Parrot> callback, World world) {
        this.metadata.setLeftShoulder(CraftBukkitObject.from(this.createParrot(callback, world)));
    }

    public void setParrotRightShoulder(Consumer<Parrot> callback, World world) {
        this.metadata.setRightShoulder(CraftBukkitObject.from(this.createParrot(callback, world)));
    }

    private PacketPlayOutMount getEntityAttachPacket(int[] entityIDs) {
        return this.createDataSerializer(data->{
            data.d(this.entityID);
            data.a(entityIDs);
            return new PacketPlayOutMount(data);
        });
    }

    private PacketPlayOutEntity.PacketPlayOutEntityLook getEntityLookPacket() {
        return new PacketPlayOutEntity.PacketPlayOutEntityLook(this.entityID, (byte)((int)(this.location.getYaw() * 256.0F / 360.0F)), (byte)((int)(this.location.getPitch() * 256.0F / 360.0F)), true);
    }

    private PacketPlayOutEntityTeleport getEntityTeleportPacket(boolean onGround) {
        return this.createDataSerializer(data->{
            data.d(this.entityID);
            data.writeDouble(this.location.getX());
            data.writeDouble(this.location.getY());
            data.writeDouble(this.location.getZ());
            data.writeByte((byte)((int)(this.location.getYaw() * 256.0F / 360.0F)));
            data.writeByte((byte)((int)(this.location.getPitch() * 256.0F / 360.0F)));
            data.writeBoolean(onGround);
            return new PacketPlayOutEntityTeleport(data);
        });
    }

    private PacketPlayOutEntityHeadRotation getEntityHeadRotatePacket() {
        return this.createDataSerializer(data->{
            data.d(this.entityID);
            data.writeByte((byte)((int)(this.location.getYaw() * 256.0F / 360.0F)));
            return new PacketPlayOutEntityHeadRotation(data);
        });
    }

    private PacketPlayOutEntityEquipment getEntityEquipmentPacket(EnumItemSlot slot, ItemStack itemStack) {
        return new PacketPlayOutEntityEquipment(this.entityID, Arrays.asList(new Pair<EnumItemSlot, ItemStack>(slot, itemStack)));
    }

    private PacketPlayOutAnimation getEntityAnimationPacket(Animation animation) {
        return this.createDataSerializer((data)->{
            data.d(this.entityID);
            data.writeByte((byte)animation.getType());
            return new PacketPlayOutAnimation(data);
        });
    }

    private PacketPlayOutEntityDestroy getEntityDestroyPacket(){
        return new PacketPlayOutEntityDestroy(this.entityID);
    }

    private PacketPlayOutEntityMetadata getEntityMetadataPacket() {
        return this.createDataSerializer((data)->{
            data.d(this.entityID);
            DataWatcher.a(((CraftMetaData)this.metadata).getList(), data);
            return new PacketPlayOutEntityMetadata(data);
        });
    }

    private PacketPlayOutNamedEntitySpawn getEntitySpawnPacket() {
        return this.createDataSerializer((data)->{
            data.d(this.entityID);
            data.a(this.profile.getId());
            data.writeDouble(this.location.getX());
            data.writeDouble(this.location.getY());
            data.writeDouble(this.location.getZ());
            data.writeByte((byte)((int)(this.location.getYaw() * 256.0F / 360.0F)));
            data.writeByte((byte)((int)(this.location.getPitch() * 256.0F / 360.0F)));
            return new PacketPlayOutNamedEntitySpawn(data);
        });
    }

    public PacketPlayOutPlayerInfo getPlayerInfoPacket(PlayerInfo playerInfo) {
        return this.createDataSerializer((data)->{
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction action = NMSConverter.convertToNMS(playerInfo);
            PacketPlayOutPlayerInfo.PlayerInfoData playerInfoData = new PacketPlayOutPlayerInfo.PlayerInfoData(this.profile, this.ping.getMilliseconds(), NMSConverter.convertToNMS(this.gamemode), CraftChatMessage.fromString(this.displayName)[0]);
            List<PacketPlayOutPlayerInfo.PlayerInfoData> list = Arrays.asList(playerInfoData);
            data.a(NMSConverter.convertToNMS(playerInfo));
            Method method = NMSConverter.convertToNMS(playerInfo).getDeclaringClass().getDeclaredMethod("a", PacketDataSerializer.class, PacketPlayOutPlayerInfo.PlayerInfoData.class);
            method.setAccessible(true);
            data.a(list, (a,b)->this.unsafe(()->method.invoke(action, a, b)));
            return new PacketPlayOutPlayerInfo(data);
        });
    }

    public int getEntityID() {
        return entityID;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public MetaData getMetadata() {
        return metadata;
    }

    public Location getLocation() {
        return location;
    }

    public Location getEyeLocation() {
        return this.location.clone().add(0, EntityTypes.bi.m().b * 0.85F, 0);
    }

    public Ping getPing() {
        return ping;
    }

    public Gamemode getGameMode() {
        return gamemode;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public MetaData getMetaData() {
        return this.metadata;
    }

    public void setMetaData(MetaData metaData) {
        this.metadata = metaData;
    }


    public void setSkin(SkinTextures skinTextures) {
        this.profile.getProperties().put("textures", new Property("textures", skinTextures.getTexture(), skinTextures.getSignature()));
    }

    public void setASyncSkinByUsername(Plugin plugin, Collection<Player> players, String username) {
        this.setASyncSkinByUsername(plugin, players, username, null);
    }

    public void setASyncSkinByUsername(Plugin plugin, Player player, String username) {
        this.setASyncSkinByUsername(plugin, player, username, null);
    }

    public void setASyncSkinByUUID(Plugin plugin, Collection<Player> players, UUID uuid) {
        this.setASyncSkinByUUID(plugin, players, uuid, null);
    }

    public void setASyncSkinByUUID(Plugin plugin, Player player, UUID uuid) {
        this.setASyncSkinByUUID(plugin, player, uuid, null);
    }

    public void setASyncSkinByUsername(Plugin plugin, Player player, String username, BiConsumer<Boolean, NPC> callback) {
        SkinTextures.getByUsername(plugin, username, (success, skin)->setASyncSkin(success, skin, player, callback));
    }

    public void setASyncSkinByUsername(Plugin plugin, Collection<Player> players, String username, BiConsumer<Boolean, NPC> callback) {
        SkinTextures.getByUsername(plugin, username, (success, skin)->setASyncSkin(success, skin, players, callback));
    }

    public void setASyncSkinByUUID(Plugin plugin, Player player, UUID uuid, BiConsumer<Boolean, NPC> callback) {
        SkinTextures.getByUUID(plugin, uuid, (success, skin)->setASyncSkin(success, skin, player, callback));
    }

    public void setASyncSkinByUUID(Plugin plugin, Collection<Player> players, UUID uuid, BiConsumer<Boolean, NPC> callback) {
        SkinTextures.getByUUID(plugin, uuid, (success, skin)->setASyncSkin(success, skin, players, callback));
    }

    private void setASyncSkin(boolean success, SkinTextures skin, Collection<Player> players, BiConsumer<Boolean, NPC> callback) {
        if(success) {
            this.setSkin(skin);
            this.reloadNPC(players);
        }
        callback.accept(success, this);
    }

    private void setASyncSkin(boolean success, SkinTextures skin, Player player, BiConsumer<Boolean, NPC> callback) {
        this.setASyncSkin(success, skin, Arrays.asList(player), callback);
    }

    public void setPing(Ping ping) {
        this.ping = ping;
    }

    public void setGameMode(Gamemode gamemode) {
        this.gamemode = gamemode;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
        GameProfile swapProfile = new GameProfile(this.profile.getId(), displayName);
        swapProfile.getProperties().putAll(this.profile.getProperties());
        this.profile = swapProfile;
    }
    private void unsafe(UnsafeRunnable run) {
        try {
            run.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T> T createDataSerializer(UnsafeFunction<PacketDataSerializer, T> callback) {
        PacketDataSerializer data = new PacketDataSerializer(Unpooled.buffer());
        T result = null;
        try {
            result = callback.apply(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            data.release();
        }
        return result;
    }

    @FunctionalInterface
    private interface UnsafeRunnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    private interface UnsafeFunction<K, T> {
        T apply(K k) throws Exception;
    }

}
