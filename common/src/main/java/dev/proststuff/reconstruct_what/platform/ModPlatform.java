package dev.proststuff.reconstruct_what.platform;

public enum ModPlatform {
    NEOFORGE("NeoForge"),
    FABRIC("Fabric");

    final String name;

    ModPlatform(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
