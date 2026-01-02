package dev.proststuff.utilitary;

import dev.proststuff.utilitary.config.Config;
import dev.proststuff.utilitary.config.ConfigFile;
import dev.proststuff.utilitary.config.utility.ConfigEnvironment;
import dev.proststuff.utilitary.config.value.ConfigBoolean;
import dev.proststuff.utilitary.config.value.ConfigString;

public class UtilitaryConfig extends Config {
    public ConfigFile startup = add("startup", ConfigEnvironment.STARTUP);
    public ConfigString startupAbout = s("about-startup", "This config will be loaded IMMIDIATELY during Utilitary mod is loading. (Value might not still be loaded, use ConfigValue$onChange() to detect changes) (does not synced)");
    public ConfigBoolean debug = b("debug", false);

    public ConfigFile common = add("common", ConfigEnvironment.COMMON);
    public ConfigString about = s("about", "Common config that should be in both the client and server (client is synced to server)");
    public ConfigString information = s("information", "For more information, see https://github.com/ProstStuff/Utilitary");
    public ConfigString tips = s("tips", "These config value class can be set not to save (not written in the file and synced)");
    public ConfigString classes = s("other-config-value", "For other values, see [dev.proststuff.utilitary.config.value] and [dev.proststuff.utilitary.config.utility.ConfigCodecs]. If the config codec does not exist, you can make your own using [dev.proststuff.utilitary.config.utility.ConfigCodec - ConfigCodec.of()]. Return null to make the config to use default value.");

    public ConfigFile client = add("client", ConfigEnvironment.CLIENT);
    public ConfigString clientAbout = s("about-client", "Client-side config (not synced)");

    public ConfigFile server = add("server", ConfigEnvironment.SERVER);
    public ConfigString serverAbout = s("about-server", "Server-side config (sync to players)");

    public UtilitaryConfig() {
        super("utilitary");

        startup
                .add(startupAbout)
                .add(debug);

        common
                .add(about)
                .add(information)
                .add(tips)
                .add(classes);

        client
                .add(clientAbout);

        server
                .add(serverAbout);
    }

    @Override
    public boolean debugEnabled() {
        return debug.get() || super.debugEnabled();
    }
}