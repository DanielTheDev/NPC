package io.github.danielthedev.npc.nms.v1_18_R1;

import io.github.danielthedev.npc.api.*;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.protocol.game.PacketPlayOutPlayerInfo;
import net.minecraft.world.entity.EntityPose;
import net.minecraft.world.entity.EnumItemSlot;
import net.minecraft.world.level.EnumGamemode;

import static io.github.danielthedev.npc.api.Pose.*;

public class NMSConverter {

    public static EnumChatFormat convertToNMS(GlowColor color) {
        return switch (color) {
            case BLACK -> EnumChatFormat.a;
            case DARK_BLUE -> EnumChatFormat.b;
            case DARK_GREEN -> EnumChatFormat.c;
            case DARK_AQUA -> EnumChatFormat.d;
            case DARK_RED -> EnumChatFormat.e;
            case DARK_PURPLE -> EnumChatFormat.f;
            case GOLD -> EnumChatFormat.g;
            case GRAY -> EnumChatFormat.h;
            case DARK_GRAY -> EnumChatFormat.i;
            case BLUE -> EnumChatFormat.j;
            case GREEN -> EnumChatFormat.k;
            case AQUA -> EnumChatFormat.l;
            case RED -> EnumChatFormat.m;
            case LIGHT_PURPLE -> EnumChatFormat.n;
            case YELLOW -> EnumChatFormat.o;
            case WHITE -> EnumChatFormat.p;
            case NONE -> EnumChatFormat.v;
        };
    }

    public static EnumGamemode convertToNMS(Gamemode gamemode) {
        return switch (gamemode) {
            case SURVIVAL -> EnumGamemode.a;
            case CREATIVE -> EnumGamemode.b;
            case ADVENTURE -> EnumGamemode.c;
            case SPECTATOR -> EnumGamemode.d;
        };
    }

    public static EnumItemSlot convertToNMS(ItemSlot itemSlot) {
        return switch (itemSlot) {
            case MAIN_HAND -> EnumItemSlot.a;
            case OFF_HAND -> EnumItemSlot.b;
            case BOOTS -> EnumItemSlot.c;
            case LEGGINGS -> EnumItemSlot.d;
            case CHESTPLATE -> EnumItemSlot.e;
            case HELMET -> EnumItemSlot.f;
        };
    }

    public static EntityPose convertToNMS(Pose pose) {
        return switch (pose) {
            case STANDING -> EntityPose.a;
            case FALL_FLYING ->EntityPose.b;
            case SLEEPING -> EntityPose.c;
            case SWIMMING -> EntityPose.d;
            case SPIN_ATTACK -> EntityPose.e;
            case CROUCHING -> EntityPose.f;
            case LONG_JUMPING -> EntityPose.g;
            case DYING -> EntityPose.h;
        };
    }

    public static Pose fromNMS(EntityPose pose) {
        return switch (pose) {
            case a -> STANDING;
            case b -> FALL_FLYING;
            case c -> SLEEPING;
            case d -> SWIMMING;
            case e -> SPIN_ATTACK;
            case f -> CROUCHING;
            case g -> LONG_JUMPING;
            case h -> DYING;
        };
    }

    public static PacketPlayOutPlayerInfo.EnumPlayerInfoAction convertToNMS(PlayerInfo playerInfo) {
        return switch (playerInfo) {
            case ADD_PLAYER -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.a;
            case UPDATE_GAME_MODE -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.b;
            case UPDATE_LATENCY -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.c;
            case UPDATE_DISPLAY_NAME -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.d;
            case REMOVE_PLAYER -> PacketPlayOutPlayerInfo.EnumPlayerInfoAction.e;
        };
    }
}
