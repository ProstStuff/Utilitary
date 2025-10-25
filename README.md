# Utilitary
When there's not enough utilities to ease modding development.

## About

---
Formerly called **RECONSTRUCT WHAT!?**,
**Utilitary** is a Fabric utility library that comes with pre-built Json config & common classes that can be found from other mods to ease Minecraft modding.

### Features

---

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
      - Custom types (Identifier, Color, UUID, Time)
      - List and maps
- **Data Generation Helper Class:** Helper interfaces to avoid typing the same code over and over.
  - `IModelGeneratable` add generation method for item model and block state model generation
  - `ITranslationGeneratable` add generation method for translation
  - `IBlockLootTableGeneratable` add generation for block loot table generation
  - `ITagProviderUtil` add some helper methods when implemented to a `TagProvider`
- **Common Classes:** Implements some data generation helper and adding some helpers
  - `BaseSyncedBlockEntity` is a block entity class that sync block entity data to the client
  - `BaseStorageBlockEntity` is a `BaseSyncedBlockEntity` class that add and manages inventory
    - Implements `IStorage`
  - `BaseBlock` & `BaseBlockWithEntity` implements `IModelGeneratable`, `ITranslationGeneratable`, and `IBlockLootTableGeneratable`
    - Drop items when the block is broken and a block entity in that position extends `BaseStorageBlockEntity`
  - `BaseItem` implements `IModelGeneratable`, `ITranslationGeneratable`
- **Utilities:** Some _utilitary_ class
  - `DirectionalVoxelShape` generates VoxelShapes rotated to other directions (north by default, east, south, west, up, down)
  - `IStorage` is an implemented inventory interface
  - `BaseRecipeJsonBuilder` is a Json recipe builder that can be extended to make custom recipes
  - `IFancyLogging` makes printing a bit more _stylized_ (if the console/log supports)
  - `DamageTypeBuilder` easily creates `DamageType` for data generation
  - `TextBuilder` allow control over Text manipulation (similar to Create's `Lang` class)
  - `GuiColor` for rendering colored ui
  - Other utils (`StringUtil`, `RandomUtil`, `RegistryUtil`)