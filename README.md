# Utilitary
When there's not enough utilities to ease modding development.

## About

---
Formerly called **RECONSTRUCT WHAT!?**,
**Utilitary** is a Fabric library that comes with pre-built Json config system and utilities to ease Minecraft modding.

---
### Features

- Some common classes that can be found in most mods
- **Config:** A Json configuration manager well-equipped with:
  - **`ConfigManager`:** The core of the config that manages writing/reading Json file
    - Gets their own folder to store `ConfigFile`s
  - **`ConfigFile`:** Represent the Json file of the config
    - Multiple environment type (common, client, server, startup)
    - Automatic loading and syncing
      - Sync common and server config environment to players
      - Compress and split large data into chunks
    - External changes detection
  - **`ConfigOption`:** Pack `ConfigValue` or another `ConfigOption`
  - **`ConfigValue`:** An extensible class that represents the value of the config
    - Utilize custom codec class (`ConfigCodec`) to allow a more versatile serialization
    - Premade include:
      - Basic types (string, boolean, integer, float, double, long)
      - Custom types (Identifier, Color, UUID, Time, Vec3d, Vec2f)
      - List and maps
- Player persistent data (`PersistentData`)
  - Persisted data can be accessed even when the player is offline
- **Utilities:** Some _utilitary_ class
  - `DirectionalVoxelShape` generates VoxelShapes rotated to other directions (north by default, east, south, west, up, down)
  - `Storage` is an implemented inventory interface
  - `BaseRecipeJsonBuilder` is a Json recipe builder that can be extended to make custom recipes
  - `FancyLogging` makes printing a bit more _stylized_ (if the console/log supports)
  - `DamageTypeBuilder` easily creates `DamageType` for data generation
  - `TextBuilder` allow control over `Text` manipulation
  - `FormatColors` to easily renders color text without needing to assign color everytime
  - Other utils (`StringUtil`, `RandomUtil`, `RegistryUtil`)