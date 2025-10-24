package dev.proststuff.utilitary.utility.config;

/**
 * Config environment types
 */
public enum ConfigEnvironment {
    COMMON,
    SERVER,
    CLIENT,
    STARTUP;

    public boolean isFor(ConfigEnvironment configEnvironment) {
        if (this == configEnvironment) return true;

        if (this == COMMON) {
            return configEnvironment.isFor(SERVER) || configEnvironment.isFor(CLIENT);
        } else if (this == CLIENT || this == SERVER) {
            return configEnvironment.isFor(COMMON);
        }

        return false;
    }
}
