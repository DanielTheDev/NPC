package io.github.danielthedev.npc.nms.v1_18_R1;

import io.github.danielthedev.npc.api.*;
import io.github.danielthedev.npc.api.bukkit.CraftBukkitObject;
import net.minecraft.core.BlockPosition;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import net.minecraft.network.syncher.DataWatcherObject;
import net.minecraft.network.syncher.DataWatcherRegistry;
import net.minecraft.network.syncher.DataWatcherSerializer;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.EntityParrot;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftParrot;
import org.bukkit.entity.Parrot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class CraftMetaData implements MetaData<IChatBaseComponent,BlockPosition,NBTTagCompound> {

    //Entity metadata
    private final DataWatcher.Item<Byte> entityState = a(0, (byte) EntityState.createMask(EntityState.DEFAULT));
    private final DataWatcher.Item<Integer> airTicks = a(1, 300);
    private final DataWatcher.Item<Optional<IChatBaseComponent>> customName = a(2, Optional.empty(), DataWatcherRegistry.f);
    private final DataWatcher.Item<Boolean> customNameVisible = a(3, false);
    private final DataWatcher.Item<Boolean> silent = a(4, false);
    private final DataWatcher.Item<Boolean> gravity = a(5, false);
    private final DataWatcher.Item<EntityPose> pose = a(6, NMSConverter.convertToNMS(Pose.STANDING));
    private final DataWatcher.Item<Integer> frozenTicks = a(7, 0); //shaking at tick 140

    //LivingEntity metadata
    private final DataWatcher.Item<Byte> handStatus = a(8, (byte)HandStatus.createMask(HandStatus.MAIN_HAND));
    private final DataWatcher.Item<Float> health = a(9, 1.0F);
    private final DataWatcher.Item<Integer> potionEffectColor = a(10, 0);
    private final DataWatcher.Item<Boolean> isPotionEffectAmbient = a(11, false);
    private final DataWatcher.Item<Integer> arrowsInEntity = a(12, 0);
    private final DataWatcher.Item<Integer> absorptionHealth = a(13, 0);
    private final DataWatcher.Item<Optional<BlockPosition>> sleepingBedLocation = a(14, Optional.empty(), DataWatcherRegistry.m);

    //Player metadata
    private final DataWatcher.Item<Float> additionalHearts = a(15, 0.0F);
    private final DataWatcher.Item<Integer> score = a(16, 0);
    private final DataWatcher.Item<Byte> skinStatus = a(17, (byte)SkinStatus.createMask(SkinStatus.ALL_ENABLED));
    private final DataWatcher.Item<Byte> hand = a(18, (byte)Hand.RIGHT.getType());
    private final DataWatcher.Item<NBTTagCompound> leftShoulder = a(19, new NBTTagCompound());
    private final DataWatcher.Item<NBTTagCompound> rightShoulder = a(20, new NBTTagCompound());

    private final List<DataWatcher.Item<?>> list;

    public CraftMetaData() {
        this.list = new ArrayList<>(Arrays.asList(
                this.entityState,
                this.airTicks,
                this.customName,
                this.customNameVisible,
                this.silent,
                this.gravity,
                this.pose,
                this.frozenTicks,
                this.handStatus,
                this.health,
                this.potionEffectColor,
                this.isPotionEffectAmbient,
                this.arrowsInEntity,
                this.absorptionHealth,
                this.sleepingBedLocation,
                this.additionalHearts,
                this.score,
                this.skinStatus,
                this.hand,
                this.leftShoulder,
                this.rightShoulder));
    }

    public EntityState[] getEntityState() {
        return EntityState.fromMask(entityState.b());
    }

    public Integer getAirTicks() {
        return airTicks.b();
    }

    public CraftBukkitObject<Optional<IChatBaseComponent>> getCustomName() {
        return CraftBukkitObject.from(customName.b());
    }

    public Boolean isCustomNameVisible() {
        return customNameVisible.b();
    }

    public Boolean isSilent() {
        return silent.b();
    }

    public Boolean hasGravity() {
        return gravity.b();
    }

    public Pose getPose() {
        return NMSConverter.fromNMS(pose.b());
    }

    public Integer getFrozenTicks() {
        return frozenTicks.b();
    }

    public HandStatus[] getHandStatus() {
        return HandStatus.fromMask(handStatus.b());
    }

    public Float getHealth() {
        return health.b();
    }

    public Integer getPotionEffectColor() {
        return potionEffectColor.b();
    }

    public Boolean isPotionEffectAmbient() {
        return isPotionEffectAmbient.b();
    }

    public Integer getArrowsInEntity() {
        return arrowsInEntity.b();
    }

    public Integer getAbsorptionHealth() {
        return absorptionHealth.b();
    }

    public CraftBukkitObject<Optional<BlockPosition>> getSleepingBedLocation() {
        return CraftBukkitObject.from(sleepingBedLocation.b());
    }

    public Float getAdditionalHearts() {
        return additionalHearts.b();
    }

    public Integer getScore() {
        return score.b();
    }

    public SkinStatus[] getSkinStatus() {
        return SkinStatus.fromMask(skinStatus.b());
    }

    public Hand getHand() {
        return Hand.fromByte(hand.b());
    }

    public CraftBukkitObject<NBTTagCompound> getLeftShoulder() {
        return CraftBukkitObject.from(leftShoulder.b());
    }

    public NBTTagCompound getRightShoulder() {
        return rightShoulder.b();
    }

    public void setEntityState(EntityState... entityState) {
        this.entityState.a((byte) EntityState.createMask(entityState));
    }

    public void setAirTicks(Integer airTicks) {
        this.airTicks.a(airTicks);
    }

    public void setCustomName(String customName) {
        this.customName.a(Optional.ofNullable(IChatBaseComponent.a(customName)));
    }

    public void setCustomNameVisible(Boolean customNameVisible) {
        this.customNameVisible.a(customNameVisible);
    }

    public void setSilent(Boolean silent) {
        this.silent.a(silent);
    }

    public void setGravity(Boolean gravity) {
        this.gravity.a(gravity);
    }

    public void setPose(Pose pose) {
        this.pose.a(NMSConverter.convertToNMS(pose));
    }

    public void setFrozenTicks(Integer frozenTicks) {
        this.frozenTicks.a(frozenTicks);
    }

    public void setShaking() {
        this.setFrozenTicks(140);
    }

    public void setHandStatus(HandStatus handStatus) {
        this.handStatus.a((byte) HandStatus.createMask(handStatus));
    }

    public void setHealth(Float health) {
        this.health.a(health);
    }

    public void setPotionEffectColor(Integer potionEffectColor) {
        this.potionEffectColor.a(potionEffectColor);
    }

    public void setIsPotionEffectAmbient(Boolean isPotionEffectAmbient) {
        this.isPotionEffectAmbient.a(isPotionEffectAmbient);
    }

    public void setArrowsInEntity(Integer arrowsInEntity) {
        this.arrowsInEntity.a(arrowsInEntity);
    }

    public void setAbsorptionHealth(Integer absorptionHealth) {
        this.absorptionHealth.a(absorptionHealth);
    }

    public void setSleepingBedLocation(CraftBukkitObject<BlockPosition> sleepingBedLocation) {
        this.sleepingBedLocation.a(Optional.ofNullable(sleepingBedLocation.getObject()));
    }

    public void setAdditionalHearts(Float additionalHearts) {
        this.additionalHearts.a(additionalHearts);
    }

    public void setScore(Integer score) {
        this.score.a(score);
    }

    public void setSkinStatus(SkinStatus... skinStatus) {
        this.skinStatus.a((byte) SkinStatus.createMask(skinStatus));
    }

    public void setHand(Hand hand) {
        this.hand.a((byte) hand.getType());
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
        this.setLeftShoulder(CraftBukkitObject.from(this.createParrot(callback, world)));
    }

    public void setParrotRightShoulder(Consumer<Parrot> callback, World world) {
        this.setRightShoulder(CraftBukkitObject.from(this.createParrot(callback, world)));
    }

    public void setLeftShoulder(CraftBukkitObject<NBTTagCompound> leftShoulder) {
        this.leftShoulder.a(leftShoulder.getObject());
    }

    public void setRightShoulder(CraftBukkitObject<NBTTagCompound> rightShoulder) {
        this.rightShoulder.a(rightShoulder.getObject());
    }

    public List<DataWatcher.Item<?>> getList() {
        return list;
    }

    private static <T> DataWatcher.Item<T> a(int index, T value) {
        DataWatcherSerializer<?> serializer = null;

        if(value instanceof Byte) {
            serializer = DataWatcherRegistry.a;
        } else if(value instanceof Float) {
            serializer = DataWatcherRegistry.c;
        } else if(value instanceof Integer) {
            serializer = DataWatcherRegistry.b;
        } else if(value instanceof String) {
            serializer = DataWatcherRegistry.d;
        } else if(value instanceof Boolean) {
            serializer = DataWatcherRegistry.i;
        } else if(value instanceof NBTTagCompound) {
            serializer = DataWatcherRegistry.p;
        } else if(value instanceof BlockPosition) {
            serializer = DataWatcherRegistry.m;
        } else if(value instanceof IChatBaseComponent) {
            serializer = DataWatcherRegistry.e;
        } else if(value instanceof EntityPose) {
            serializer = DataWatcherRegistry.s;
        }
        return a(index, value, (DataWatcherSerializer<T>)serializer);
    }

    private static <T> DataWatcher.Item<T> a(int index, T value, DataWatcherSerializer<T> serializer) {
        return new DataWatcher.Item<T>(new DataWatcherObject<T>(index, serializer), value);
    }

}
