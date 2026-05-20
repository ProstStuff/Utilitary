package dev.proststuff.utilitary.api.config.impl;

import dev.proststuff.utilitary.api.config.ConfigFile;

public interface ConfigFileChild extends ConfigSerializable {
    void setConfigFile(ConfigFile configFile);
    ConfigFile getConfigFile();
}
