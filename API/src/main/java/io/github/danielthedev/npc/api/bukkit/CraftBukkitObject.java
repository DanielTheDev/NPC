package io.github.danielthedev.npc.api.bukkit;

public class CraftBukkitObject<T> {

    private final T object;

    public CraftBukkitObject(T object) {
        this.object = object;
    }

    public T getObject() {
        return object;
    }

    public static <A> CraftBukkitObject<A> from(A a) {
        return new CraftBukkitObject<A>(a);
    }
}
