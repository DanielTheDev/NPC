package io.github.danielthedev.npc.api;

import io.github.danielthedev.npc.api.bukkit.CraftBukkitObject;

import java.util.Optional;

public interface MetaData<A,B,C> {

    Integer getAirTicks();

    CraftBukkitObject<Optional<A>> getCustomName();

    Boolean isCustomNameVisible();

    Boolean isSilent();

    Boolean hasGravity();

    Pose getPose();

    Integer getFrozenTicks();

    HandStatus[] getHandStatus();

    Float getHealth();

    Integer getPotionEffectColor();

    Boolean isPotionEffectAmbient();

    Integer getArrowsInEntity();

    Integer getAbsorptionHealth();

    CraftBukkitObject<Optional<B>> getSleepingBedLocation();

    Float getAdditionalHearts();

    Integer getScore();

    SkinStatus[] getSkinStatus();

    Hand getHand();

    CraftBukkitObject<C> getLeftShoulder();

    void setRightShoulder(CraftBukkitObject<C> rightShoulder);

    void setLeftShoulder(CraftBukkitObject<C> leftShoulder);

    void setEntityState(EntityState... entityState);

    void setAirTicks(Integer airTicks);

    void setCustomName(String customName);

    void setCustomNameVisible(Boolean customNameVisible);

    void setSilent(Boolean silent);

    void setGravity(Boolean gravity);

    void setPose(Pose pose);

    void setFrozenTicks(Integer frozenTicks);

    void setShaking(boolean shaking);

    void setHandStatus(HandStatus handStatus);

    void setHealth(Float health);

    void setPotionEffectColor(Integer potionEffectColor);

    void setIsPotionEffectAmbient(Boolean isPotionEffectAmbient);

    void setArrowsInEntity(Integer arrowsInEntity);

    void setAbsorptionHealth(Integer absorptionHealth);

    void setSleepingBedLocation(CraftBukkitObject<B> sleepingBedLocation);

    void setAdditionalHearts(Float additionalHearts);

    void setScore(Integer score);

    void setSkinStatus(SkinStatus... skinStatus);

    void setHand(Hand hand);
}
