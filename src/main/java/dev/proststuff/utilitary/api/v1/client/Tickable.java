package dev.proststuff.utilitary.api.v1.client;

public interface Tickable {
    boolean canTick();
    void tick();
}
