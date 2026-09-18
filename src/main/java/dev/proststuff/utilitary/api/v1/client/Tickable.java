package dev.proststuff.utilitary.api.v1.client;

public interface Tickable {
    boolean shouldTick();
    void tick();
}
