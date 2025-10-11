# RECONSTRUCT WHAT?
THERE'S NOTHING LEFT!

---

RECONSTRUCT WHAT is an over-engineered Minecraft Json-based config library, while remain optimized... I think...

## Why use this mod?
To be honest I don't encourage you to use this library really,
there are some better config library,
I just can't find a good one, so I'm making my own.

### Features
- Expandable `ConfigValue`
  - Can be extended for more compatibility between other objects
  - Use custom codec, allowing full control over serialization and deserialization
  - Premade `ConfigValue`:
    - Basic types (booleans, integers, doubles, longs, strings)
    - Color
    - Enum
    - List
    - Map
    - ResourceLocation
    - UUID
    - Vec2 & Vec3
- Multi-configuration files
  - Why limited to only one common, client, server, and startup file? Why not more?
   - Every `ConfigManager` get their own folder according to their names
    - (`config/arthritis/`, `config/TheCoolerArcheries/`)
  - `ConfigHolder` also get their own file and stored to their `ConfigManager` folder
    - (`config/arthriits/hardmodes.json`, `WORLD/serverconfig/TheCoolerArcheries/Bows&Gun.json`).
- Nesting group
  - Use `ConfigGroup` to group values together to make them neatly packed
    - Can also nest another `ConfigGroup`
    - ^ Please do not create singularity from this, no preventive measure has been made as of current version
- Automated syncing & initialization
  - Common, client, and server configs are initialized automatically by the library
    - ^ There is a slim chance that your configs registered late, it is low but never zero.
  - Your `ConfigManager` and its instances must be initialized first
  - Common and server configs are synced to the client
    - Compress and split large data into chunks
  - Detects external changes during runtime
- Configuration screens (WIP)

### Notes:
- Startup config must be loaded manually
```java
static {
  // Make sure you have `startup` config created
  // from `.makeConfig("startup", ConfigHelper.ConfigType.STARTUP)`
  // or `new ConfigManager(MOD_NAME, true, ...)`
  ConfigHolder STARTUP = MANAGER.getStartup().add(RECONSTRUCT_WHAT);

  // Register onLoad listener
  STARTUP.onLoaded(manager -> {
    for (String string : RECONSTRUCT_WHAT.get()) {
      manager.info(IFancyLogging.LogType.ACTION, string);
    }
  });
    
  // you can ignore `null`, it just asks for a MinecraftServer
  // (for server config only as serverconfig are relative to world)
  // true indicates whether the config will save later after load or not
  // ^ saves new value (and delete unregistered value | unintended)
  MANAGER.loadSpecific(ConfigHelper.ConfigType.STARTUP, null, true);
}
```
- This config library is mainly made for my mods,
this means updates of this library follows the development of my other mod.
- Experimental mod; use at your own risk